module.exports = {
    outputDir: './public',
    devServer: {
        proxy: {
            '/apiUser': {
                 target:'http://localhost:9090/',
                //target: 'http://106.14.106.73:8080/',
                // target: 'http://se-backend-server:8080/',
                changeOrigin: true,
                pathRewrite: {
                    '^/apiUser': '/api'
                }
            },
            '/apiBook': {
                target:'http://localhost:9091/',
                //target: 'http://106.14.106.73:8080/',
                // target: 'http://se-backend-server:8080/',
                changeOrigin: true,
                pathRewrite: {
                    '^/apiBook': '/api'
                }
            },
            '/apiBorrow': {
                target:'http://localhost:9092/',
                //target: 'http://106.14.106.73:8080/',
                // target: 'http://se-backend-server:8080/',
                changeOrigin: true,
                pathRewrite: {
                    '^/apiBorrow': '/api'
                }
            },
            '/apiComment': {
                target:'http://localhost:9093/',
                //target: 'http://106.14.106.73:8080/',
                // target: 'http://se-backend-server:8080/',
                changeOrigin: true,
                pathRewrite: {
                    '^/apiComment': '/api'
                }
            },
            '/apiLog': {
                target:'http://localhost:9099/',
                //target: 'http://106.14.106.73:8080/',
                // target: 'http://se-backend-server:8080/',
                changeOrigin: true,
                pathRewrite: {
                    '^/apiLog': '/api'
                }
            },
            '/apiViolation': {
                target:'http://localhost:9094/',
                //target: 'http://106.14.106.73:8080/',
                // target: 'http://se-backend-server:8080/',
                changeOrigin: true,
                pathRewrite: {
                    '^/apiViolation': '/api'
                }
            },
            '/api8080': {
                target:'http://localhost:8080/',
                //target: 'http://106.14.106.73:8080/',
                // target: 'http://se-backend-server:8080/',
                changeOrigin: true,
                pathRewrite: {
                    '^/api': '/api'
                }
            },
        }
    }
}
