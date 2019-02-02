// Define a new component called module
Vue.component('module', {
  props : [ 'module' ],
  methods: {
    newAttribute: function() {
      if (this.module.attributes === undefined) {
        this.module.attributes = [];
      }
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
    
    <!-- Display the baselines -->
    <h2>Baselines</h2>
    <table>
      <thead>
        <tr>
          <th class="BaselineNameColumn">Name</th>
          <th class="BaselineCountColumn">Count</th>
        </tr>
      </thead>
      <tr v-for="baseline in module.baselines" :key="baseline.id">
        <td><strong>{{ baseline.name }}: </strong></td>
        <td>{{ baseline.requirements == undefined ? 0 : baseline.requirements.length }}</td>
      </tr>
    </table>
    
    <!-- Display the attributes -->
    <h2 v-if="attributesVisible">Attributes</h2>
    <button v-on:click="newAttribute">New Attribute</button>
    <table v-if="attributesVisible">
      <thead>
        <tr>
          <th class="AttributeNameColumn">Name</th>
          <th class="AttributeTypeColumn">Type</th>
        </tr>
      </thead>
      <tr v-for="attribute in module.attributes" :key="attribute.id">
        <td><strong>{{ attribute.name }}: </strong></td>
        <td>{{ attribute.type }}</td>
      </tr>
    </table>
    </div>`
});