/**
 * js压缩.
 * User: xlzhou2@iflytek.com
 * Date: 14-9-4
 * Time: 下午7:42
 * To change this template use File | Settings | File Templates.
 */

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
    var fs = require('fs'),
        fileutil = require('./file'),
        path = require('path');
    var confEnv = process.argv[2] ? process.argv[2] : 'devconfig';
    var config = require('./config')[confEnv];
    var jsp = require("uglify-js").parser;
    var pro = require("uglify-js").uglify;
    //静态文件存放目录
    var staticFolder = config.staticFolder;
    //根目录
    var currentPath = path.join(__dirname, '../');

    //模板文件目录
    var srcFolders = config.srcCompression.split(',');

    /**
     * 遍历文件夹
     */
    function handleFile() {
        //将相对目录转为绝对目录
        for (var s = 0; s < srcFolders.length; s++) {
            srcFolders[s] = (path.normalize(path.join(currentPath, srcFolders[s])));
        }
        //
        srcFolders.forEach(function (item, idx) {
            fileutil.walk(item, 4, function (currPath, depth) {
                if (path.extname(currPath) == '.js') {
                    var resolve =path.resolve();
                    var outPath = currPath.replace(resolve,staticFolder).replace(/\\/g, '/');
                    buildOne(currPath, outPath);
                }
            });
        });
    }

    // 读取一个文件，压缩之
    function buildOne(fileIn, fileOut) {
        var origCode = fs.readFileSync(fileIn, 'utf8');
        var ast = jsp.parse(origCode);
        ast = pro.ast_mangle(ast);
        ast = pro.ast_squeeze(ast);
        var finalCode = pro.gen_code(ast);
        fs.writeFileSync(fileOut, finalCode, 'utf8');
    }

    function compression() {
        handleFile();
    }

    nc.compression = compression;
});
