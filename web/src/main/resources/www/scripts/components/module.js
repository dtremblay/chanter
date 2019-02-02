// Define a new component called module
Vue.component('module', {
  props : [ 'module' ],
  computed: {
    attributesVisible: function() {
      return (this.module.attributes.length > 0);
    }
  },
  template : `<div class="container">
    <h2>Module Details</h2>
    <table class="table table-condensed">
      <tbody>
        <tr><th scope="row">Name: </th><td>{{ module.name }} </td></tr>
        <tr><th scope="row">Id: </th><td>{{ module.id }}</td></tr>
        <tr><th scope="row">Description: </th><td>{{ module.description }}</td></tr>
      </tbody>
      <tfoot>
        <tr><td scope="row" colspan="2">&nbsp;</td></tr>
      </tfoot>
    </table>
    
    <!-- Display the baselines -->
    <h2>Baselines</h2>
    <table class="table table-condensed">
      <thead>
        <tr>
          <th class="BaselineNameColumn">Name</th>
          <th class="BaselineCountColumn">Requirement Count</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="baseline in module.baselines" :key="baseline.id">
          <th scope="row">{{ baseline.name }}: </th>
          <td>{{ baseline.requirements == undefined ? 0 : baseline.requirements.length }}</td>
        </tr>
      </tbody>
      <tfoot>
        <tr><td scope="row" colspan="2">&nbsp;</td></tr>
      </tfoot>
    </table>
    
    <!-- Display the attributes -->
    <h2 v-if="attributesVisible">Attributes</h2>
    <router-link :to="'/modules/' + this.module.id + '/attributes/create'" class="btn btn-primary">New Attribute</router-link>
    <table v-if="attributesVisible" class="table table-condensed">
      <thead>
        <tr>
          <th scope="col">Attribute Name</th>
          <th scope="col">Attribute Type</th>
          <th scope="col">Action</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="attribute in module.attributes" :key="attribute.id">
          <th scope="row">{{ attribute.name }}: </th>
          <td>{{ attribute.type }}</td>
          <td><router-link :to="$route.params.moduleId + '/attributes/' + attribute.name" class="glyphicon glyphicon-pencil"></router-link></td>
        </tr>
      </tbody>
      <tfoot>
        <tr><td scope="row" colspan="3">&nbsp;</td></tr>
      </tfoot>
    </table>
    
    </div>`
});