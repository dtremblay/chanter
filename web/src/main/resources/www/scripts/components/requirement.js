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
          <div class="col-sm-offset-2 col-sm-10">
            <button v-on:click="appendRequirement" class="btn btn-primary btn-default">Save</button>
            <button v-on:click="back" class="btn btn-cancel">Cancel</button>
          </div>
        </div>
      </form>
    </div>
    `
});