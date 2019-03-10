  var compStatistics = {
    methods: {
      // Create a new module
      newModule: function() {
        this.$router.push("/modules/create");
      },
      // Import a CSV, PDF, Excel or MS Word Document
      importFile: function () {
      },
      generateReport: function() {
      }
    },
    computed: {
    },
    template: `
    <div class="container">
      <div class="row">
        <h2>Statistics</h2>
        <table class="table">
          <tbody>
          <tr><th scope="row">Modules: </th><td>{{ this.$store.getters.moduleCount }} </td></tr>
          <tr><th scope="row">Baselines: </th><td>{{ this.$store.getters.baselineCount }} </td></tr>
          </tbody>
          <tfoot>
            <tr><td scope="row" colspan="2">&nbsp;</td></tr>
          </tfoot>
        </table>
      </div>
      <div class="row">
        <div class="col-md-2">
          <button v-on:click="newModule" class="btn btn-primary btn-lg">New Module</button>
        </div>
        <div class="col-md-2 dropdown">
          <button class="btn btn-primary btn-lg dropdown-toggle" type="button" id="importDropDownMenu" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
            Import File
            <span class="caret"></span>
          </button>
          <ul class="dropdown-menu" aria-labelledby="importDropDownMenu">
            <li><router-link to="/import?type=pdf">Import from PDF</router-link></li>
            <li><router-link to="/import?type=xls">Import from Excel</router-link></li>
            <li><router-link to="/import?type=csv">Import from CSV</router-link></li>
            <li><router-link to="/import?type=wrd">Import from MS Word</router-link></li>
          </ul>
        </div>
      </div>
    </div>`
  };
  
  /*****************************************************************************
    MODULE COMPONENT
  *****************************************************************************/
  var compModule = { 
    props: ['moduleId'],
    data: function() {
      return {
        selMod: undefined
      };
    },
    methods: {
      findModule: function(id) {
        var self = this;
        this.$store.state.modules.forEach(function(m, idx) {
          m.expanded = (m.id === id);
          if (m.id === id) {
            if (m.attributes === undefined) {
              Vue.set(m, "attributes", []);
            }
            self.selMod = m;
          }
        });
        if (self.selMod == undefined) {
          // Create a new module
          var id = this.$store.getters.moduleCount + 1;
          self.selMod = {id: "id" + id, attributes:[], name: 'New Module',
            expanded:true, baselines: [{name:"current", id:"c" + id}]};
        }
        return self.selMod;
      }
    },
    template: '<module v-bind:module="findModule(moduleId)"/>'
  };
  
  /*****************************************************************************
    BASELINE COMPONENT
  *****************************************************************************/
  var compBaseline = {
    props: ['moduleId','baselineId'],
    data: function() {
      return {
        selBL: undefined
      }
    },
    methods: {
      findBaseline(mId, bId) {
        var self=this;
        this.$store.state.modules.forEach(function(m){
          m.expanded = (m.id === mId);
          if (m.id === mId) {
            m.baselines.forEach(function(b){
              if (b.id === bId) {
                b.module = {name: m.name, id:m.id};
                b.attributes = m.attributes;
                if (b.requirements === undefined) {
                  Vue.set(b, "requirements", []);
                }
                self.selBL = b;
              }
            });
          }
        });
        return self.selBL;
      }
    },
    template: '<baseline v-bind:baseline="findBaseline(moduleId,baselineId)"/>'
  };
  
  /*****************************************************************************
    ATTRIBUTE COMPONENT
  *****************************************************************************/
  var compAttribute = {
    props: ['moduleId','attributeId'],
    data: function() {
      return {
        //modules: modules,
        module: null,
        selAtt: undefined
      }
    },
    methods: {
      findAttribute: function(mId, aName) {
        var self=this;
        this.$store.state.modules.forEach(function(m) {
          if (m.id === mId) {
            self.module = m;
            self.module.expanded = (m.id === mId);
            if (m.attributes === undefined) {
              Vue.set(m, "attributes", []);
            } else {
              m.attributes.forEach(function(a) {
                if (a.name === aName) {
                  self.selAtt = a;
                }
              });
            }
          }
        });
        if (self.selAtt == undefined) {
          // Create a new attribute
          var id = self.module.attributes.length + 1;
          self.selAtt = {id: "id" + id, name:'New Attribute ' + id, type: "String"};
        }
        return self.selAtt;
      }
    },
    template: '<attribute v-bind:attribute="findAttribute(moduleId,attributeId)"/>'
  };
  /*****************************************************************************
    REQUIREMENT COMPONENT
  *****************************************************************************/
  var compRequirement = {
    props: ['moduleId','baselineId','requirementId'],
    data: function(){
      return {
        modAttrs: undefined,
        currentBL: undefined,
        selReq: undefined 
      }
    },
    methods: {
      findRequirement: function(mId, bId, rId){
        var self=this;
        this.$store.state.modules.forEach(function(m){
          m.expanded = (m.id === mId);
          if (m.id === mId) {
            self.modAttrs = m.attributes;
            m.baselines.forEach(function(b){
              if (b.id === bId) {
                self.currentBL = b;
                if (b.requirements === undefined) {
                  Vue.set(b, "requirements", []);
                } else {
                  b.requirements.forEach(function(r) {
                    if (r.id === rId) {
                      self.selReq = r;
                    }
                  });
                }
              }
            });
          }
        });
        if (self.selReq == undefined) {
          // Create a new requirement, but don't assign it to the collection
          var id = self.currentBL.requirements.length + 1;
          self.selReq = {id: 'id' + id, description:'New Requirement ' + id };
          if (self.modAttrs !== undefined) {
            self.modAttrs.forEach(function(att){
              self.selReq[att.name] = "blank";
            });
          }
        }
        return self.selReq;
      }
    },
    template: '<requirement v-bind:requirement="findRequirement(moduleId,baselineId,requirementId)"/>'
  };
  
  var compImportModule = {
    props: ['import-type'],
    methods: {
    },
    template: '<import-module v-bind:import-type=""/>'
  };
  
  // Add Dynamic Routers
  const router = new VueRouter({
    routes: [
      { path: '/home', component: compStatistics },
      { path: '/modules/:moduleId',
        component: compModule,
        props: true
      },
      { path: '/modules/:moduleId/baselines/:baselineId',
        component: compBaseline,
        props: true
      },
      { path: '/modules/:moduleId/baselines/:baselineId/requirements/:requirementId',
        component: compRequirement,
        props: true
      },
      { path: '/modules/:moduleId/attributes/:attributeId',
        component: compAttribute,
        props: true
      },
      { path: '/about', component: { template: '<div>About</div>'} },
      { path: '/import', component: compImportModule },
      { path: '/', redirect: '/home' }
    ]
  });
  
  // Create a store
  const store = new Vuex.Store({
    state: {
      modules: [
        {expanded: false, name: "Module 1", id: "id1", attributes: [{name:'attr1',type:'String'}], 
          baselines: [ {name:"current", id: "c1"}]},
        {expanded: false, name: "Module 2", id: "id2",
          baselines: [{name:"SRR", id: "b1"},{name:"PDR", id: "b2"}, {name:"current", id: "c2"}]}
      ]
    },
    getters: {
      moduleCount: state => {
        return state.modules.length;
      },
      baselineCount: state => {
        var total = 0;
        state.modules.forEach(function(m) {
          total += m.baselines.length;
        })
        return total;
      }
    },
    mutations: {
      createModule(state, module) {
        Vue.set(state.modules, state.modules.length, module);
      },
      createAttribute(state, updateAttributeObject) {
        Vue.set(updateAttributeObject.module.attributes, updateAttributeObject.module.attributes.length, updateAttributeObject.attribute);
      }
    }
  });
  new Vue({
    el:'#app',
    router,
    store
  });