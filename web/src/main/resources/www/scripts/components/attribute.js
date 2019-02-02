Vue.component('attribute', {
  props: ['attribute'],
  methods: {
    appendAttribute: function() {
      if (this.$route.params.attributeId === "create") {
        Vue.set(this.$parent.module.attributes, this.$parent.module.attributes.length, this.attribute);
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
          <label class="col-sm-2 control-label">Name:</label>
          <div class="col-sm-10">
            <input class="form-control" v-model="attribute.name"/>
          </div>
        </div>
        <div class="form-group">
          <label class="col-sm-2 control-label">Type:</label>
          <div class="col-sm-10">
            <select class="form-control" v-model="attribute.type">
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