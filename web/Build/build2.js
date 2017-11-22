var util = require('./util'),
    fileutil = require('./file'),
    path = require('path'),
    url = require('url'),
    copy_files = require('./node_modules/node_copy/copy.js'),
    os = require('os');
var confEnv = process.argv[2] ? process.argv[2] : 'devconfig';
var config = require('./config')[confEnv];

//根目录
var currentPath = path.join(__dirname, '../');
console.info('当前根目录' + currentPath);

//静态文件存放目录
var staticFolder = config.staticFolder;

//把所有的图片文件复制到公共资源里
var staticRawFolder = path.join(currentPath, '/module/zhixueStudent');
//console.log(staticRawFolder)
if (os.type() == "Linux") {
    fileutil.copyFolder(staticRawFolder, path.join(staticFolder, '/module/zhixueStudent'));
} else {
    // windows 环境调用bat文件做拷贝
    copy_files.copy(staticRawFolder, path.join(staticFolder, '/module/zhixueStudent'),"build2");
}
console.info('//把zhixueStudent的图片文件复制到公共资源里');
console.log('\x1B[41m%s\x1B[49m',"build2 完成");



