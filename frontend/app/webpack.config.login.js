var path = require('path')

module.exports = {
  entry: {
    app: ['./login']
  },
  output: {
    path: path.join(__dirname, '/../public'),
    filename: 'loginBundle.js'
  },
  module:{
    loaders:[
      {
        test: /\.js|.jsx$/,
        loader:'babel-loader',
        exclude: /node_modules/,
        query:{
          presets:[
            'babel-preset-es2015',
            'babel-preset-react'
          ]
        }
      }
    ]
  }
}
