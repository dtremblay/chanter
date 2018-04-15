// Define a new component called module
Vue.component('module', {
  props : [ 'module' ],
  template : `<div class="module">
    <h1>Module</h1>
    <table>
      <tr><td><strong>Name: </strong></td><td>{{ module.name }} </td></tr>
      <tr><td><strong>Id: </strong></td><td>{{ module.id }}</td></tr>
    </table>
    <h2>Attributes</h2>
    <table><tr v-for="attribute in module.attributes"><td><strong>{{ attribute.name }}: </strong></td><td>{{ attribute.type }}</td></tr></table>
    </div>`
})