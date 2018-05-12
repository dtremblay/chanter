// Define a new component called module
Vue.component('baseline', {
  props : [ 'baseline' ],
  methods: {
    newRequirement: function() {
      var id = this.baseline.requirements.length + 1;
      //Vue.set(this.baseline.requirements, id-1 , {id: 'id' + id, description:'New Requirement ' + id });
      this.baseline.requirements.push({id: 'id' + id, description:'New Requirement ' + id });
    }
  },
  computed: {
    reqCount: function() {
      return this.baseline.requirements.length;
    },
    reqVisible: function() {
      return this.baseline.requirements !== undefined;
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
      <tr v-for="req in baseline.requirements">
        <td><strong>{{ req.id }}: </strong></td>
        <td>{{ req.description }}</td>
      </tr>
    </table>
    </div>`
});