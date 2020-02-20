<script>
  import { onMount } from "svelte";
  import Menu from "./UI/Menu.svelte";
  import Requirements from "./UI/Requirements.svelte";
  import Statistics from "./UI/Statistics.svelte";
  import Reports from "./UI/Reports.svelte";
  import Help from "./UI/Help.svelte";
  import ImportFile from "./UI/ImportFile.svelte";
  import NotImplemented from "./UI/NotImplemented.svelte";

  // Setting forms
  import Preferences from "./UI/Preferences.svelte";
  import CreateModule from "./UI/CreateModule.svelte";
  import EditModule from "./UI/EditModule.svelte";
  import PasswordSettings from "./UI/Password.svelte";

  let currentRoute = "statistics";
  let currentModuleName;
  let currentModule;
  let currentComponent = Statistics;
  let importModal = false;

  export let version; // this value is passed from main.js
  export let baseServer; // this value is passed from main.js
  export let path; // if the user presses refresh, allow the application to return to the correct page
  let menuShown = true;

  let moduleSummaries = [];
  let moduleCount = 0;
  let baselineCount = 0;

  onMount(async function() {
    const response = await fetch(baseServer);
    moduleSummaries = await response.json();
    moduleCount = moduleSummaries.length;
    var bc = 0;
    moduleSummaries.forEach(m => {
      m.expanded = false;
      bc += m.baselines.length;
    });
    baselineCount = bc;
    resumePath();
  });

  function resumePath() {
    //extract the path starting from the #
    var hash = path.hash;
    if (hash && hash.length > 1) {
      currentRoute = hash.substring(1);
    }
    locateRoute(currentRoute);
  }
  function selectMenu(event) {
    currentRoute = event.detail.name;
    moduleSummaries.forEach(m => {
      m.expanded = false;
    });
    locateRoute(currentRoute);
  }

  function selectModule(event) {
    currentRoute = event.detail.name;
    locateRoute(currentRoute);
  }
  function selectBaseline(event) {
    currentRoute = event.detail.name;
    locateRoute(currentRoute);
  }

  async function findModuleById(id) {
    if (id !== "new") {
      console.log("Loading module by id: ", id);
      var mod = moduleSummaries.find(m => m.guid === id);
      if (mod) {
        console.log("Loading module by name: ", mod.name);
        const response = await fetch(baseServer + "/" + mod.name);
        currentModule = await response.json();
      } else {
        console.error("Module not found: " + id);
      }
    }
  }

  async function findBaselineById(moduleId, baselineId) {
    console.log("Find Baseline: " + baselineId);
  }

  function locateRoute(route) {
    switch (route) {
      case "newrequirement":
        currentComponent = CreateRequirement;
        break;
      case "statistics":
        currentComponent = Statistics;
        break;
      case "reports":
        currentComponent = Reports;
        break;
      case "preferences":
        currentComponent = Preferences;
        break;
      case "password":
        currentComponent = PasswordSettings;
        break;
      case "help":
        currentComponent = Help;
        break;
      case "newmodule":
        currentModuleName = "new";
        currentComponent = CreateModule;
        break;
      default:
        var all = route;
        var subroute;
        var pos = all.indexOf("|");
        if (pos > 0) {
          route = all.substring(0, pos);
          subroute = all.substring(pos + 1);
          console.log("subroute: ", route, subroute);
          moduleSummaries.forEach(m => {
            m.expanded = route == m.guid;
          });
          var baseline = findBaselineById(route, subroute);
          // Set the component to requirements
          currentComponent = Requirements;
        } else {
          var mod = findModuleById(route);
          if (mod) {
            moduleSummaries.forEach(m => {
              m.expanded = route == m.guid;
            });

            currentModule = mod;
            currentModuleName = mod.name;

            // Set the component to Edit Module
            currentComponent = EditModule;
          }
        }
    }
  }

  function handleRoute(event) {
    var route = event.detail;
    if (route !== undefined && route.length === 0) {
      route = "statistics";
    }
    currentRoute = route;
    locateRoute(route);
  }

  function saveModule(event) {
    let newMod = event.detail;
    newMod.expanded = false;
    if (newMod.guid == "new") {
      // Add a new module
      moduleSummaries = [...moduleSummaries, newMod];
      moduleCount += 1;
      baselineCount += 1;
    } else {
      // update the module
      var module = moduleSummaries.find(m => m.id === newMod.id);
      var index = moduleSummaries.indexOf(module);
      moduleSummaries.splice(index, 1, newMod);
      moduleSummaries = [...moduleSummaries];
    }
    currentRoute = "statistics";
    locateRoute("statistics");
    currentModuleName = "";
  }
  async function deleteModule(event) {
    let response = await fetch(baseServer + "/" + event.detail.name, {
      method: "delete",
      body: {}
    });
    let result = await response.json();
    console.log("deleteModule:", result);
  }
  function showImportDialog(event) {
    console.log("import:", event.detail);
    importModal = true;
  }
</script>

<style>
  .footer {
    bottom: 1px;
    position: absolute;
    width: inherit;
    height: 40px;
    padding: 10px;
  }
  span.icon {
    cursor: pointer;
  }
</style>

<svelte:head>
  <title>Chanter Requirements System</title>
  <link
    rel="stylesheet"
    href="https://cdn.jsdelivr.net/npm/bulma@0.8.0/css/bulma.min.css" />
  <script defer src="https://use.fontawesome.com/releases/v5.3.1/js/all.js">

  </script>
</svelte:head>
<div class="tile tile-ancestor">
  <aside
    class="tile is-parent is-vertical is-2 menu"
    class:is-active={menuShown}>
    <ul class="menu-list">
      <Menu
        name="statistics"
        on:selectMenu={selectMenu}
        selected={currentRoute === 'statistics'}>
        Statistics
      </Menu>

      {#each moduleSummaries as module}
        <Menu
          name={module.guid}
          on:selectMenu={selectModule}
          expandable="true"
          selected={currentRoute === module.guid}
          bind:expanded={module.expanded}>
          {module.name}
        </Menu>
        {#if module.expanded == true}
          <li>
            <ul>

              {#each module.baselines as baseline}
                <Menu
                  name={module.guid + '|' + baseline.guid}
                  selected={currentRoute === module.guid + '|' + baseline.guid}
                  on:selectMenu={selectBaseline}
                  {baseline}>
                  {baseline.name}
                </Menu>
              {/each}
            </ul>
          </li>
        {/if}
      {/each}

      <Menu
        name="reports"
        on:selectMenu={selectMenu}
        selected={currentRoute === 'reports'}>
        Reports
      </Menu>
      <Menu
        name="help"
        on:selectMenu={selectMenu}
        selected={currentRoute === 'help'}>
        Help
      </Menu>
      <p class="menu-label">Setup</p>
      <li>
        <ul>
          <Menu
            name="preferences"
            on:selectMenu={selectMenu}
            selected={currentRoute === 'preferences'}>
            Preferences
          </Menu>
          <Menu
            name="password"
            on:selectMenu={selectMenu}
            selected={currentRoute === 'password'}>
            Change Password
          </Menu>
        </ul>
      </li>
    </ul>
  </aside>
  {#if moduleSummaries}
    <main class="tile is-parent is-vertical is-10 is-fullwidth">
      <svelte:component
        this={currentComponent}
        module={currentModule}
        {moduleCount}
        {baselineCount}
        moduleName={currentModuleName}
        {baseServer}
        on:route={handleRoute}
        on:import={showImportDialog}
        on:saveModule={saveModule}
        on:deleteModule={deleteModule} />
    </main>
  {/if}
  <ImportFile bind:isActive={importModal} />
</div>

<div class="footer level">
  <div class="level-left">Chanter Requirements System, Version {version}</div>
  <div class="level-right">Copyright 2020 © DaT Systems</div>
</div>
