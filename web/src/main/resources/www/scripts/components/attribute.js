Vue.component('attribute', {
  props: ['attribute'],
  methods: {
    appendAttribute: function() {
      if (this.$route.params.attributeId === "create") {
        this.$store.commit("createAttribute", {module: this.$parent.module, attribute: this.attribute});
      }
      this.back();
    },
    back: function() {
      this.$router.push('/modules/' + this.$route.params.moduleId);
    }
  },
  template: `
    <div class="container">
      <h2>Attribute</h2>
      <form class="form-horizontal">
        <div class="form-group">
          <label class="col-sm-2 control-label" for="attributeName">Name</label>
          <div class="col-sm-10">
            <input class="form-control" id="attributeName" v-model="attribute.name"/>
          </div>
        </div>
        <div class="form-group">
          <label class="col-sm-2 control-label" for="attributeType">Type</label>
          <div class="col-sm-10">
            <select class="form-control" id="attributeType" v-model="attribute.type">
              <option>String</option>
              <option>Boolean</option>
              <option>Number</option>
            </select>
          </div>
        </div>
        <div class="form-group">
          <div class="col-sm-offset-2 col-sm-10">
            <button v-on:click="appendAttribute" class="btn btn-primary btn-default">Save</button>
            <button v-on:click="back" class="btn btn-cancel">Cancel</button>
          </div>
        </div>
      </form>
    </div>
  `
});