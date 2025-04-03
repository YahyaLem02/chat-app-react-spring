const path = require("path");

module.exports = {
    resolve: {
        fallback: {
            fs: false,
            path: require.resolve("path-browserify"),
            os: require.resolve("os-browserify/browser"),
            http: require.resolve("stream-http"),
            https: require.resolve("https-browserify"),
            crypto: require.resolve("crypto-browserify"),
            net: false,
        },
    },
};