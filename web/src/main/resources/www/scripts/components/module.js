// Define a new component called module
Vue.component('module', {
  props : [ 'module' ],
  computed: {
    attributesVisible: function() {
      return (this.module.attributes.length > 0);
    }
  },
  methods: {
    appendModule: function() {
      if (this.$route.params.moduleId === "create") {
        Vue.set(this.$parent.modules, this.$parent.modules.length, this.module);
        this.$router.push("/modules/" + this.module.id);
      }
    }
  },
  template : `<div class="container">
    <h2>Module Details</h2>
    <form class="form-horizontal">
      <div class="form-group">
        <label class="col-sm-2 control-label">Id</label>
        <div class="col-sm-10">
          <span class="form-control">{{ module.id === undefined ? 'New' : module.id }}</span>
        </div>
      </div>
      <div class="form-group">
        <label class="col-sm-2 control-label">Name</label>
        <div class="col-sm-10">
          <input class="form-control" v-model="module.name"/>
        </div>
      </div>
      <div class="form-group">
        <label class="col-sm-2 control-label">Description</label>
        <div class="col-sm-10">
          <textarea class="form-control" v-model="module.description" rows="4">
          </textarea>
        </div>
      </div>
      <div v-if="this.$route.params.moduleId === 'create'" class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
          <button v-on:click="appendModule" class="btn btn-primary btn-default">Save</button>
        </div>
      </div>
    </form>
    
    <!-- Display the baselines -->
    <h2 v-if="this.$route.params.moduleId !== 'create'">Baselines</h2>
    <table v-if="this.$route.params.moduleId !== 'create'" class="table table-condensed">
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
    <h2 v-if="attributesVisible && this.$route.params.moduleId !== 'create'">Attributes</h2>
    <router-link :to="'/modules/' + this.module.id + '/attributes/create'" v-if="this.$route.params.moduleId !== 'create'" class="btn btn-primary">New Attribute</router-link>
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