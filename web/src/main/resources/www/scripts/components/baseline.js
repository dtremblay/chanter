// Define a new component called module
Vue.component('baseline', {
  props : [ 'baseline' ],
  methods: {
    newRequirement: function() {
      var id = this.baseline.requirements.length + 1;
      var requirement = {id: 'id' + id, description:'New Requirement ' + id };
      this.baseline.attributes.forEach(function(att){
        requirement[att.name] = "blank";
      });
      Vue.set(this.baseline.requirements, id-1 , requirement);
    }
  },
  computed: {
    reqCount: function() {
      return this.baseline.requirements.length;
    },
    reqVisible: function() {
      return (this.baseline.requirements.length > 0);
    }
  },
  template : `<div class="baseline">
    <h1>Baseline</h1>
    <table>
      <tr><td><strong>Name: </strong></td><td>{{ baseline.name }} </td></tr>
      <tr><td><strong>Id: </strong></td><td>{{ baseline.id }}</td></tr>
      <tr><td><strong>Module Name: </strong></td><td>{{ baseline.module.name }}</td></tr>
      <tr><td><strong>Count: </strong></td><td>{{ reqCount }}</td></tr>
    </table>
    
    <h2 v-if="reqVisible">Requirements</h2>
    <button v-on:click="newRequirement">New Requirements</button>
    
    <table>
      <thead>
        <tr>
          <th class="RequirementIdColumn">Name</th>
          <th class="RequirementNameColumn">Count</th>
          <th v-for="attribute in baseline.attributes">
          {{ attribute.name }}
          </th>
        </tr>
      </thead>
      <tr v-for="req in baseline.requirements">
        <td><strong>{{ req.id }}: </strong></td>
        <td>{{ req.description }}</td>
        <td v-for="attribute in baseline.attributes">
          {{ req[attribute.name] === undefined ? '' : req[attribute.name] }}
        </td>
      </tr>
    </table>
    </div>`
});