const { override, useBabelRc } = require('customize-cra');

module.exports = override(
    useBabelRc(),
    (config) => {
        if (config.devServer) {
            config.devServer.setupMiddlewares = (middlewares, devServer) => {
                if (!devServer) {
                    throw new Error('webpack-dev-server is not defined');
                }

                // 필요한 미들웨어 설정
                devServer.app.use((req, res, next) => {
                    console.log('middleware');
                    next();
                });

                return middlewares;
            };
        }
        return config;
    }
);
