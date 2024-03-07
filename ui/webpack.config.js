const config = {
    mode: 'development', // "production" | "development" | "none"
    resolve: {
      extensions: ['*', '.mjs', '.js', '.json', '.ts', '.jsx']
    },
    module: {
      rules: [
        {
          test: /\.mjs$/,
          include: /node_modules/,
          type: 'javascript/auto'
        }
      ]
    }
  }
  
  module.exports = config