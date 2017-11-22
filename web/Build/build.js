var util = require('./util'),
    fileutil = require('./file'),
    path = require('path'),
    url = require('url'),
    copy_files = require('./node_modules/node_copy/copy.js'),
    os = require('os');
var confEnv = process.argv[2] ? process.argv[2] : 'devconfig';
var version = process.argv[3] ? process.argv[3] : '';
version=version.replace(/\./g,"");
var config = require('./config')[confEnv];
var compression = require('./compression');

/**
 * 模板文件关系表
 * {
 * '/studycenter/homework/home/index/html':{
 *      content:"<html>",模板内容
 *      isParted:true 是不是被嵌套的模板
 * }}
 */
var templateList = {};

var INCLUDE_REGEX_G = /\{include\s+file=(?:(?:"([^"\\]*)"|'([^'\\]*)'|([^\s'"]+)+))\}/ig;
var INCLUDE_REGEX = /\{include\s+file=(?:(?:"([^"\\]*)"|'([^'\\]*)'|([^\s'"]+)+))\}/i;
var TEMPLATE_REGEX_G = /<!--\s*template\s*-->([\s|\S]*?)<!--\s*\/template\s*-->/ig;
var SCRIPT_TAG_REGEX_G = /<script(?:\s+type="text\/javascript")*\s+src="([\s|\S]*?)"[^>]*?>(?:[\n\r\t ]*?)<\/script>/ig;
var SCRIPT_TAG_REGEX = /<script(?:\s+type="text\/javascript")*\s+src="([\s|\S]*?)"[^>]*?>(?:[\n\r\t ]*?)<\/script>/i;
var LINK_TAG_REGEX_G = /<link(?:[\s|\S]*?)href="([\s|\S]*?)"[^>]*?>/ig;
var LINK_TAG_REGEX = /<link(?:[\s|\S]*?)href="([\s|\S]*?)"[^>]*?>/i;
var IMG_TAG_REGEX_G = /<img[^<>]* src=[\"']([^\"']*)[\"'][^<>]*>/ig;
var IMG_TAG_REGEX = /<img[^<>]* src=[\"']([^\"']*)[\"'][^<>]*>/i;

//根目录
var currentPath = path.join(__dirname, '../');
console.info('当前系统环境：' + os.type());
console.info('当前根目录' + currentPath);
console.info("当前前端模块版本"+version);

if (os.type() != "Linux") {
    console.info("                                                            ");
    console.info("                   _ooOoo_ ");
    console.info("                  o8888888o ");
    console.info("                  88\" . \"88 ");
    console.info("                  (| -_- |) ");
    console.info("                  O\\  =  /O ");
    console.info("               ____/`---'\\____ ");
    console.info("             .'  \\\\|     |//  `. ");
    console.info("            /  \\\\|||  :  |||//  \\  ");
    console.info("           /  _||||| -:- |||||-  \\  ");
    console.info("           |   | \\\\\\  -  /// |   | ");
    console.info("           | \\_|  ''\\---/''  |   | ");
    console.info("           \\  .-\\__  `-`  ___/-. / ");
    console.info("         ___`. .'  /--.--\\  `. . __ ");
    console.info("      .\"\" '<  `.___\\_<|>_/___.'  >'\"\". ");
    console.info("     | | :  `- \\`.;`\\ _ /`;.`/ - ` : | | ");
    console.info("     \\  \\ `-.   \\_ __\\ /__ _/   .-` /  /");
    console.info("======`-.____`-.___\\_____/___.-`____.-'======");
    console.info("                   `=---='");
    console.info("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
    console.info("\t    佛祖保佑       永无BUG");
    console.info("                                                            ");
    console.info("\t写字楼里写字间，写字间里程序员； ");
    console.info("\t程序人员写程序，又拿程序换酒钱。");
    console.info("\t酒醒只在网上坐，酒醉还来网下眠；");
    console.info("\t酒醉酒醒日复日，网上网下年复年。");
    console.info("\t但愿老死电脑间，不愿鞠躬老板前；");
    console.info("\t奔驰宝马贵者趣，公交自行程序员。");
    console.info("\t别人笑我忒疯癫，我笑自己命太贱；");
    console.info("\t不见满街漂亮妹，哪个归得程序员？");
    console.info("                                                            ");
}
//模板文件目录
var srcFolders = config.srcFolders.split(',');
//将相对目录转为绝对目录
for (var s = 0; s < srcFolders.length; s++) {
    srcFolders[s] = (path.normalize(path.join(currentPath, srcFolders[s])));
}

//时间戳
var timeStamp = new Date().toISOString();
//静态文件存放目录
var staticFolder = config.staticFolder;
//静态文件网址前缀
var staticUrlPrefix = config.staticUrlPrefix.replace("{version}",version);
//是否压缩
var isCompression = config.compression;

//把所有的图片文件复制到公共资源里
var staticRawFolder = path.join(currentPath, '/module/global');
//console.log(staticRawFolder)
//判断平台操作系统类型
if (os.type() == "Linux") {
    fileutil.copyFolder(staticRawFolder, path.join(staticFolder, '/module/global'));
} else {
    // windows 环境调用bat文件做拷贝
    copy_files.copy(staticRawFolder, path.join(staticFolder, '/module/global'), "build");
}
console.info('//把所有的图片文件复制到公共资源里');

//生成所有html文件的列表
srcFolders.forEach(function (item, idx) {
    fileutil.walk(item, 4, function (currPath, depth) {
        if (path.extname(currPath) == '.html') {
            var content = fileutil.getContentText(currPath, 'utf-8');
            var thisObj = {
                children: []
            };
            thisObj.content = content;
            templateList[currPath] = thisObj;
        }
    });
});
console.info('//生成所有html文件的列表');
buildTree();
//遍历html文件列表，抽取出其中的组件文件，并把内容中的demo部分删除，只保留被调用的文本
function findChild(t, arr) {
    var _templ = templateList[t];
    for (var i = 0; i < _templ.children.length; i++) {
        arr.push(_templ.children[i]);
        if (templateList[_templ.children[i]].children.length > 0) {
            findChild(_templ.children[i], arr);
        }
    }
}

/*暂时删除include标签中的内嵌关联
 var topList = [];
 for (var s in templateList) {
 if (templateList[s].children.length > 0) {
 topList.push({name: s, children: []});
 }
 }
 for (var i = 0; i < topList.length; i++) {
 findChild(topList[i].name, topList[i].children);
 }
 for (var i = 0; i < topList.length; i++) {
 var t = topList[i].name;
 //把外围页面的html代码里自动包含内嵌的文件相关javascript
 var _html = templateList[t].content;
 var _temp = '';
 for (var k = 0; k < topList[i].children.length; k++) {
 var _t = topList[i].children[k];
 var _inJSPath = path.join(path.dirname(_t), 'index.js');
 _temp += '<script type="text/javascript" src="' + _inJSPath + '"></script>';
 }

 if (_html.match(/<\/body>([\s|\S]*?)<\/html>/i)) {
 _html = _html.replace(/(<\/body>([\s|\S]*?)<\/html>)/ig, _temp + '$1');
 } else {
 _html += _temp;
 }
 templateList[t].content = _html;
 }*/
/*for (var s in templateList) {
 findChild(s);
 }*/

console.info('//遍历html文件列表，抽取出其中的组件文件，并把内容中的demo部分删除，只保留被调用的文本');
replaceTag('img', templateList);
replaceTag('script', templateList);
replaceTag('link', templateList);
/**
 * 为所有的模板列表生成父子关系的树形表
 */
function buildTree() {
    for (var t in templateList) {
        var content = templateList[t].content;
        var includeTag, tempCont;
        while (includeTag = INCLUDE_REGEX_G.exec(content)) {
            var tempPath;
            //从正则取来的字符串网址
            //可能有三种形式 /xxx/xxx/xx,xxx/xxx/xx,../../xxxx/xxx，所以需要做二次处理
            if (includeTag[1].indexOf('.') === 0) {
                tempPath = path.resolve(path.join(path.dirname(t), includeTag[1]));
            } else {
                tempPath = path.resolve(path.join(currentPath, includeTag[1]));
            }
            templateList[t].children.push(tempPath);
            if (templateList[tempPath]) {
                tempCont = templateList[tempPath].content.match(TEMPLATE_REGEX_G)
                try {
                    templateList[tempPath].content = tempCont.join('');
                } catch (e) {
                    console.error(e.message);
                }
                //templateList[tempPath].parent = t;
            } else {
                console.error('include中定义的文件未找到' + tempPath + t);
            }
        }
    }
}
/**
 * 节点类型
 * @param tagType
 */
function replaceTag(tagType, filesTemp) {
    var matchRegexpG, matchRegexp;
    switch (tagType) {
        case 'img':
            matchRegexp = IMG_TAG_REGEX;
            matchRegexpG = IMG_TAG_REGEX_G;
            break;
        case 'link':
            matchRegexp = LINK_TAG_REGEX;
            matchRegexpG = LINK_TAG_REGEX_G;
            break;
        case 'script':
            matchRegexp = SCRIPT_TAG_REGEX;
            matchRegexpG = SCRIPT_TAG_REGEX_G;
            break;
    }
    for (var t in filesTemp) {
        var content = filesTemp[t].content;
        var linkTag, finalPath;
        var linkPath, targetPath;
        var tags = content.match(matchRegexpG);
        //console.log(s);
        if (tags) {
            for (var i = 0; i < tags.length; i++) {
                linkTag = tags[i].match(matchRegexp);
                linkPath = linkTag[1]
                if (linkPath.indexOf('{') == -1) {
                    var htmlFilePath = path.dirname(t);
                    //如果已写了根目录，就不用再相对于当前html的地址再做路径转换
                    //todo:如果是http网址也不处理
                    if (linkPath.indexOf('http://') == 0 || linkPath.indexOf('https://') == 0) {
                        finalPath = linkPath;
                    } else {
                        //如果不是根目录的写法
                        if (linkPath.indexOf('/') !== 0 && linkPath.indexOf(':\\') !== 0) {
                            linkPath = path.normalize(path.join(htmlFilePath, linkPath));
                        } else {
                            //如果是根目录的写法
                            //有两种情况，一种是被上一次内嵌机制处理过的，另一种是开发过程中直接写为相对于模板根目录
                            if (linkPath.indexOf(currentPath) !== 0) {
                                linkPath = path.join(currentPath, linkPath);
                            }
                        }
                        //计算节点已计算出的路径和当前Build目录的相对路径
                        linkPath = path.relative(currentPath, linkPath);
                        linkPath = linkPath.replace(/\\/ig, '\/');
                        finalPath = staticUrlPrefix + linkPath;
                    }
                    //console.log(finalPath);

                    content = content.replace(linkTag[0], linkTag[0].replace(matchRegexp, function ($0, $1) {
                        return $0.replace($1, finalPath + "?" + timeStamp);
                    }));
                    if (fileutil.isExist(path.join(currentPath, linkPath))) {
                        targetPath = path.join(staticFolder, linkPath);
                        //生成静态文件的目录
                        fileutil.mkdirP.sync(path.dirname(targetPath));
                        if (path.extname(targetPath) == '.js') {
                            fileutil.writeContentText(
                                targetPath,
                                fileutil.getContentText(path.join(currentPath, linkPath)),
                                'utf-8'
                            )
                        }
                    }
                } else {
                    //console.log(linkPath);
                }
            }
        }

        templateList[t].content = content;
    }

}

//把html文本写入到待部署的文件夹
var deployFolder = config.publicPrefix;
for (var t in templateList) {
    var _t = path.relative(currentPath, t);
    fileutil.mkdirP.sync(path.join(deployFolder, path.dirname(_t)));
    fileutil.writeContentText(path.join(deployFolder, _t), templateList[t].content, 'utf-8')
}

if (isCompression) {
    console.log("压缩--start");
    compression.compression();
    console.log("压缩--end");
}
console.log('\x1B[41m%s\x1B[49m', "build 完成");



