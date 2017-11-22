!function (factory) {
    if (typeof require === 'function' && typeof exports === 'object' && typeof module === 'object') {
        var target = module['exports'] || exports;
        factory(target);
    } else if (typeof define === 'function' && define['amd']) {
        define(['exports'], factory);
    } else {
        factory(window['NC'] = {});
    }
}(function (exports) {
    var nc = typeof exports !== 'undefined' ? exports : {};
    var fs = require('fs'), path = require('path');
    var statSync = fs.statSync;
    var iconv = require('iconv-lite');//todo simon
    var CONT_REGEX = /<script\s+type="remark-template"\s+id="([\w|-|_|@|#]+?)">([\s|\S]+?)<\/script>/ig;

    function isExist(path) {
        return fs.existsSync(path);
    }

    /**
     * 判断路径是否是目录
     * @param path
     * @returns {*}
     */
    function isDirectory(path) {
        if (!this.isExist(path)) {
            return false;
        }
        var stat = fs.lstatSync(path);
        return stat.isDirectory();
    }

    /**
     * 同步写入文件内容
     * @param path
     * @param txt
     */
    function writeContentText(path, txt, encoding) {
        var data = txt || '';
        encoding = encoding || 'utf-8';
        fs.writeFileSync(path, data, encoding);
    }


    function mkdirP(p, mode, f, made) {
        if (typeof mode === 'function' || mode === undefined) {
            f = mode;
            mode = 0777 & (~process.umask());
        }
        if (!made) made = null;

        var cb = f || function () {
        };
        if (typeof mode === 'string') mode = parseInt(mode, 8);
        p = path.resolve(p);

        fs.mkdir(p, mode, function (er) {
            if (!er) {
                made = made || p;
                return cb(null, made);
            }
            switch (er.code) {
                case 'ENOENT':
                    mkdirP(path.dirname(p), mode, function (er, made) {
                        if (er) cb(er, made);
                        else mkdirP(p, mode, cb, made);
                    });
                    break;

                // In the case of any other error, just see if there's a dir
                // there already.  If so, then hooray!  If not, then something
                // is borked.
                default:
                    fs.stat(p, function (er2, stat) {
                        // if the stat fails, then that's super weird.
                        // let the original error be the failure reason.
                        if (er2 || !stat.isDirectory()) cb(er, made)
                        else cb(null, made);
                    });
                    break;
            }
        });
    }

    mkdirP.sync = function sync(p, mode, made) {
        if (mode === undefined) {
            mode = 0777 & (~process.umask());
        }
        if (!made) made = null;

        if (typeof mode === 'string') mode = parseInt(mode, 8);
        p = path.resolve(p);

        try {
            fs.mkdirSync(p, mode);
            made = made || p;
        }
        catch (err0) {
            switch (err0.code) {
                case 'ENOENT' :
                    made = sync(path.dirname(p), mode, made);
                    sync(p, mode, made);
                    break;

                // In the case of any other error, just see if there's a dir
                // there already.  If so, then hooray!  If not, then something
                // is borked.
                default:
                    var stat;
                    try {
                        stat = fs.statSync(p);
                    }
                    catch (err1) {
                        throw err0;
                    }
                    if (!stat.isDirectory()) throw err0;
                    break;
            }
        }

        return made;
    };

    /**
     * 同步读文件内容
     * @param path
     * @returns {string}
     */
    function getContentText(path, encoding) {
        var txt = '';
        encoding = encoding || 'utf-8';
        try {
            txt = fs.readFileSync(path, encoding);
            /*            txt = fs.readFileSync(path, 'binary');
             txt = iconv.decode(new Buffer(txt), encoding);*/
        } catch (ex) {
            txt = ''
        }
        return txt;
    }


    /*
     * 复制目录中的所有文件包括子目录
     * @param{ String } 需要复制的目录
     * @param{ String } 复制到指定的目录
     */
    var copyFolder = function (src, dst) {
        try {
            // 读取目录中的所有文件/目录
            var files = fs.readdirSync(src);
            files.forEach(function (path) {
                var _src = src + '/' + path,
                    _dst = dst + '/' + path,
                    readable, writable;

                var stat = statSync(_src);
                // 判断是否为文件
                if (stat.isFile()) {
                    // 创建读取流
                    readable = fs.createReadStream(_src);
                    // 创建写入流
                    writable = fs.createWriteStream(_dst);
                    // 通过管道来传输流
                    readable.pipe(writable);
                }
                // 如果是目录则递归调用自身
                else if (stat.isDirectory()) {
                    mkdirP.sync(_dst);
                    copyFolder(_src, _dst);
                }

            });
        } catch (ex) {


        }
    };

    /**
     * 取到模板片段
     * @param txt
     * @returns {*}
     */
    function filterHtmlContent(txt) {
        var _templates = [], _arr;
        //处理已替换的taglib标签
        while ((_arr = CONT_REGEX.exec(txt)) != null) {
            var id = _arr[1];
            var cont = _arr[2];
            _templates.push({id: id, cont: cont});
        }
        return _templates;
    }

    /**
     * 递归处理文件,文件夹
     * @param folder 路径
     * @param floor 层数
     * @param handleFile 文件,文件夹处理函数
     */
    function walk(folder, floor, handleFile, depth) {
        depth = depth || 0;
        //console.log('正在遍历的目录'+folder);
        var files = fs.readdirSync(folder);
        for (var i = 0; i < files.length; i++) {
            var _depth = depth || 0;
            var tmpPath = path.join(folder, files[i]);
            var stats = fs.statSync(tmpPath);
            if ((_depth < floor) && stats.isDirectory()) {
                _depth++;
                arguments.callee(tmpPath, floor, handleFile, _depth);
            } else {
                handleFile(tmpPath, _depth);
            }
        }
    }

    /**
     * 获取文件夹的子文件夹
     * @param path 路径
     * @returns {Array} 子文件夹
     */
    function getSubFolder(path) {
        var folders = [];
        var files = fs.readdirSync(path);
        for (var i = 0; i < files.length; i++) {
            var tmpPath = path + '/' + files[i];
            var stats = fs.statSync(tmpPath);
            if (stats.isDirectory()) {
                folders.push(files[i]);
            }
        }
        return folders;
    }

    nc.walk = walk;
    nc.isDirectory = isDirectory;
    nc.filterHtmlContent = filterHtmlContent;
    nc.getContentText = getContentText;
    nc.writeContentText = writeContentText;
    nc.getSubFolder = getSubFolder;
    nc.isExist = isExist;
    nc.mkdirP = mkdirP;
    nc.copyFolder = copyFolder;
});
