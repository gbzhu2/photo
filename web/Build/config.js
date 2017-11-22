exports.devconfig={ //开发自己构建
    'srcFolders':'photo,module',//须要处理的目录
    'publicPrefix':'D:/Project/photo/webapp/src/main/webapp/WEB-INF/views',//发布目录
    'staticFolder':'D:/Project/photo/webapp/src/main/webapp/public', //静态文件存放目录
    'staticUrlPrefix':'/photo/public/',//静态文件网址前缀
    'srcCompression': 'photo',            //js 文件压缩目录
    'compression': false            //js 文件是否压缩
};
exports.dailyconfig={ // 每日构建
    'srcFolders':'opmsystem,module',//须要处理的目录
    'publicPrefix':'/home/jenkins_home/workspace/edu-elp-daily-ZX-opmsystem/opmsystem/webapp/src/main/webapp/WEB-INF/views',//发布目录
    'staticFolder':'/home/jenkins_home/workspace/edu-elp-daily-ZX-opmsystem/opmsystem/webapp/src/main/webapp/public', //静态文件存放目录
    'staticUrlPrefix':'/opmsystem/public/',//静态文件网址前缀
    'srcCompression': 'opmsystem',            //js 文件压缩目录
    'compression': false            //js 文件是否压缩
};
exports.testconfig={ //测试构建版本
    'srcFolders':'opmsystem,module',//须要处理的目录
	'publicPrefix':'/home/jenkins_home/workspace/edu-elp-test-ZX-opmsystem-deploy/opmsystem/webapp/src/main/webapp/WEB-INF/views',//发布目录
    'staticFolder':'/home/jenkins_home/workspace/edu-elp-test-ZX-opmsystem-deploy/opmsystem/webapp/src/main/webapp/public', //静态文件存放目录
    'staticUrlPrefix':'/opmsystem/public/',//静态文件网址前缀
    'srcCompression': 'opmsystem',            //js 文件压缩目录
    'compression': false            //js 文件是否压缩
};
exports.testconfig2={ //持续构建平台配置
    'srcFolders':'opmsystem,module',//须要处理的目录
    'publicPrefix':'/disk1/jenkins/workspace/ZXW.ZZW-Application.TESTOpmsystem.TEST-opmsystem.test/webapp/src/main/webapp/WEB-INF/views',//发布目录
    'staticFolder':'/disk1/jenkins/workspace/ZXW.ZZW-Application.TESTOpmsystem.TEST-opmsystem.test/webapp/src/main/webapp/public', //静态文件存放目录
    'staticUrlPrefix':'/opmsystem/public/',//静态文件网址前缀
    'srcCompression': 'opmsystem',            //js 文件压缩目录
    'compression': false            //js 文件是否压缩
};
exports.productpreconfig={ //预上线构建
    'srcFolders':'opmsystem,module',//须要处理的目录
    'publicPrefix':'/home/jenkins_home/workspace/edu-elp-productpre-ZX-opmsystem-deploy/opmsystem/webapp/src/main/webapp/WEB-INF/views',//发布目录
    'staticFolder':'/home/jenkins_home/workspace/edu-elp-productpre-ZX-opmsystem-deploy/opmsystem/webapp/src/main/webapp/public', //静态文件存放目录
    'staticUrlPrefix':'/opmsystem/public/',//静态文件网址前缀
    'srcCompression': 'opmsystem',            //js 文件压缩目录
    'compression': false            //js 文件是否压缩
};
exports.productconfig={ //金盘上线构建
    'srcFolders':'opmsystem,module',//须要处理的目录
    'publicPrefix':'/home/jenkins_home/workspace/edu-elp-product-ZX-opmsystem-deploy/opmsystem/webapp/src/main/webapp/WEB-INF/views',//发布目录
    'staticFolder':'/home/jenkins_home/workspace/edu-elp-product-ZX-opmsystem-deploy/opmsystem/webapp/src/main/webapp/public', //静态文件存放目录
    'staticUrlPrefix':'/opmsystem/public/',//静态文件网址前缀
    'srcCompression': 'opmsystem',            //js 文件压缩目录
    'compression': false            //js 文件是否压缩
};
exports.productconfig2={ //金盘上线构建
    'srcFolders':'opmsystem,module',//须要处理的目录
    'publicPrefix':'/disk1/jenkins/workspace/ZXW.ZZW-Application.PROOpmsystem.PRO-opmsystem.pro/webapp/src/main/webapp/WEB-INF/views',//发布目录
    'staticFolder':'/disk1/jenkins/workspace/ZXW.ZZW-Application.PROOpmsystem.PRO-opmsystem.pro/webapp/src/main/webapp/public', //静态文件存放目录
    'staticUrlPrefix':'/opmsystem/public/',//静态文件网址前缀
    'srcCompression': 'opmsystem',            //js 文件压缩目录
    'compression': false            //js 文件是否压缩
};
exports.productconfig3={ //cdn文件上传打包配置
    'srcFolders':'opmsystem,module',//须要处理的目录
    'publicPrefix':'/disk1/jenkins/workspace/ZXW.ZZW-Application.PROOpmsystem.PRO-opmsystem.pro.cdn/opmsystem/webapp/src/main/webapp/WEB-INF/views',//发布目录
    'staticFolder':'/disk1/jenkins/workspace/ZXW.ZZW-Application.PROOpmsystem.PRO-opmsystem.pro.cdn/opmsystem/webapp/src/main/webapp/public', //静态文件存放目录
    'staticUrlPrefix':'/opmsystem/public/',//静态文件网址前缀
    'srcCompression': 'opmsystem',            //js 文件压缩目录
    'compression': false            //js 文件是否压缩
};
