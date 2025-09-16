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
          type: 'javascript/auto',
          enforce: 'pre',
          use: ['source-map-loader'],
          exclude: /node_modules\/react-datepicker/
        }
      ]
    }
  }
  
  // module.exports = config

  module.exports = {
    
    ignoreWarnings: [
      {
        module: /node_modules\/react-datepicker/,
      },
    ],
  };