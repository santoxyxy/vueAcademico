module.exports = {
  root: true,
  env: {
    node: true
  },
  extends: [
    "plugin:vue/vue3-essential",
    "eslint:recommended"
  ],
  parser: "vue-eslint-parser",
  parserOptions: {
    parser: "@babel/eslint-parser",
    requireConfigFile: false
  },
  rules: {
    // 💡 Añade esta línea para permitir nombres de una sola palabra específicos
    "vue/multi-word-component-names": ["error", {
      "ignores": ["index", "Index", "default"] // Permite estos nombres de una sola palabra
    }]
  }
};
