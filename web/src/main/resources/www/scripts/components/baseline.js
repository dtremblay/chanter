// Define a new component called module
Vue.component('baseline', {
  props : {
    'baseline': Object
  },
  computed: {
    reqCount: function() {
      return this.baseline.requirements.length;
    },
    reqVisible: function() {
      return (this.baseline.requirements.length > 0);
    }
  },
  template : `<div class="container">
    <h1>Baseline</h1>
    <table class="table table-condensed">
      <tbody>
        <tr><td><strong>Name: </strong></td><td>{{ baseline.name }} </td></tr>
        <tr><td><strong>Id: </strong></td><td>{{ baseline.id }}</td></tr>
        <tr><td><strong>Module Name: </strong></td><td>{{ baseline.module.name }}</td></tr>
        <tr><td><strong>Count: </strong></td><td>{{ reqCount }}</td></tr>
      </tbody>
    </table>
    
    <h2 v-if="reqVisible">Requirements</h2>
    <router-link v-if="this.baseline.name === 'current'" :to="'/modules/' + baseline.module.id + '/baselines/' + baseline.id + '/requirements/create'" class="btn btn-lg btn-primary">New Requirement</router-link>
    <table v-if="reqVisible" class="table table-condensed">
      <thead>
        <tr>
          <th class="RequirementIdColumn" scope="col">#</th>
          <th class="RequirementNameColumn" scope="col">Description</th>
          <th v-for="attribute in baseline.attributes" scope="col">
          {{ attribute.name }}
          </th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="req in baseline.requirements">
          <th scope="row">{{ req.id }}: </th>
          <td>{{ req.description }}</td>
          <td v-for="attribute in baseline.attributes">
            {{ req[attribute.name] === undefined ? '' : req[attribute.name] }}
          </td>
          <td><router-link :to="'/modules/' + $route.params.moduleId + '/baselines/' + $route.params.baselineId + '/requirements/' + req.id" class="glyphicon glyphicon-pencil"></router-link></td>
        </tr>
      </tbody>
    </table>
    </div>`
});