Vue.component('requirement', {
  props: ['requirement'],
  methods: {
    appendRequirement: function() {
      if (this.$route.params.requirementId === "create") {
        Vue.set(this.$parent.currentBL.requirements, this.$parent.currentBL.requirements.length, this.requirement);
      }
      this.back();
    },
    back: function() {
      this.$router.push('/modules/' + this.$route.params.moduleId + '/baselines/' + this.$route.params.baselineId);
    }
  },
  template: `
    <div class="container">
      <h2>Requirement</h2>
      <form class="form-horizontal">
        <div class="form-group">
          <label class="col-sm-2 control-label">Id</label>
          <div class="col-sm-10">
            <span class="form-control">{{requirement.id}}</span>
          </div>
        </div>
        <div class="form-group">
          <label class="col-sm-2 control-label" for="reqDescription">Description</label>
          <div class="col-sm-10">
            <textarea rows="3" class="form-control" id="reqDescription" v-model="requirement.description"></textarea>
          </div>
        </div>
        <!-- Attributes -->
        <div class="form-group" v-for="attribute in this.$parent.modAttrs" :key="attribute.name">
          <label class="col-sm-2 control-label" :for="'req' + attribute.name">{{attribute.name}}</label>
          <div class="col-sm-10">
            <input rows="3" class="form-control" :id="'req' + attribute.name" v-model="requirement[attribute.name]"/>
          </div>
        </div>
        <div class="form-group">
          <div class="col-sm-offset-2 col-sm-10">
            <button v-on:click="appendRequirement" class="btn btn-primary btn-default">Save</button>
            <button v-on:click="back" class="btn btn-cancel">Cancel</button>
          </div>
        </div>
      </form>
    </div>
    `
});