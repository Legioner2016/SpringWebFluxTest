module.exports = {
  entry: './app/index.jsx',
  output: {
    filename: 'bundle.js',
    path: '/home/legioner/workspace_new/WebFluxTest-2/src/main/resources/templates/'
  },
  module: {
  rules: [
    {
      test: /\.jsx$/,
      use: {
        loader: 'babel-loader'
      }
    }
  ]
}
}
