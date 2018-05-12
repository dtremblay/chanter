// Define a new component called module
Vue.component('module', {
  props : [ 'module' ],
  methods: {
    newAttribute: function() {
      var id = this.module.attributes.length + 1;
      Vue.set(this.module.attributes, id-1, {name:'New Attribute ' + id, type: "String"});
    }
  },
  computed: {
    attributesVisible: function() {
      return this.module.attributes !== undefined;
    }
  },
  template : `<div class="module">
    <h1>Module</h1>
    <table>
      <tr><td><strong>Name: </strong></td><td>{{ module.name }} </td></tr>
      <tr><td><strong>Id: </strong></td><td>{{ module.id }}</td></tr>
    </table>
    <h2 v-if="attributesVisible">Attributes</h2>
    <button v-on:click="newAttribute">New Attribute</button>
    <table v-if="module.attributes !== undefined">
      <tr v-for="attribute in module.attributes" :key="attribute.id">
        <td><strong>{{ attribute.name }}: </strong></td>
        <td>{{ attribute.type }}</td>
      </tr>
    </table>
    </div>`
});