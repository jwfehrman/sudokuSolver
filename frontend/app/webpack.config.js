var path = require('path')

module.exports = {
  entry: {
    app: ['./app']
  },
  output: {
    path: path.join(__dirname, '/../public'),
    filename: 'bundle.js'
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
