<svelte:head>
	<title>Chanter Requirements System</title>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bulma@0.8.0/css/bulma.min.css">
    <script defer src="https://use.fontawesome.com/releases/v5.3.1/js/all.js"></script>
</svelte:head>

<script>
	import { onMount } from "svelte";
	import Menu from './UI/Menu.svelte';
	import Requirements from './UI/Requirements.svelte';
	import Statistics from './UI/Statistics.svelte';
	import Reports from './UI/Reports.svelte';
	import Help from './UI/Help.svelte';
	import ImportFile from './UI/ImportFile.svelte';
	import NotImplemented from './UI/NotImplemented.svelte';

	// Setting forms
	import Preferences from './UI/Preferences.svelte';
	import CreateModule from './UI/CreateModule.svelte';
	import EditModule from './UI/EditModule.svelte';
	import PasswordSettings from './UI/Password.svelte';

	let currentRoute = "statistics";
	let currentModuleName;
	let currentModule;
	let currentComponent = Statistics;
	let importModal = false;

	export let version; // this should be coming from the package.json
	export let path; // if the user presses refresh, allow the application to return to the correct page
	let menuShown = true;

	let modules = [];
	let moduleCount = 0;
	let baselineCount = 0;

	onMount(async function() {
        const response = await fetch('http://localhost:8181/chanter');
		modules = await response.json();
		moduleCount = modules.length;
		var bc = 0;
		modules.forEach(m => {
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
		locateRoute(currentRoute);
	}

	function findModuleById(name) {
		return modules.find(m => m.guid == name);
	}
	function selectModule(event) {
		if (currentModule){
			currentModule.expanded = false;
		}
		currentRoute = event.detail.name;
		var mod = findModuleById(currentRoute);
		if (mod) {
			currentModuleName = mod.name;
			
			currentModule = mod;
			console.log(mod);
		}
		
		locateRoute(currentRoute);
	}
	function selectBaseline(event) {
		currentRoute = event.detail.name;
		locateRoute(currentRoute);
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
				currentModuleName = 'new';
				currentComponent = CreateModule;
				break;
			default:
				var all = route;
				var subroute;
				var pos = all.indexOf('|');
				if (pos>0) {
					route = all.substring(0,pos);
					subroute = all.substring(pos+1)
					console.log("subroute: ", route, subroute);
				}
				var mod = findModuleById(route);
				if (mod) {
					mod.expanded = true;
					currentModule = mod;
					currentModuleName = mod.name;
				}
				if (subroute) {
					// find the baseline and return the requirements
					///////currentBaseline
					
					currentComponent = Requirements;
				} else {
					currentComponent = EditModule;
					//currentComponent.loadModule();
				}
		}
	}

	function handleRoute(event) {
		var route = event.detail;
		if (route !== undefined && route.length ===0 ) {
			route = "statistics";
		}
		currentRoute = route;
		locateRoute(route);
	}

	function saveModule(event) {
		var newMod = event.detail;
		newMod.expanded = false;
		if (newMod.guid == 'new') {
			// Add a new module
			modules = [...modules, newMod];
			moduleCount += 1;
			baselineCount += 1;
		} else {
			// update the module
			var module = modules.find(m => m.id === newMod.id);
			var index = modules.indexOf(module);
			modules.splice(index,1, newMod);
			modules = [...modules];

		}
		currentRoute = 'statistics';
		locateRoute('statistics');
		currentModuleName = '';
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
<div class="tile tile-ancestor"> 
	<aside class="tile is-parent is-vertical is-2 menu" class:is-active={menuShown}>
		<ul class="menu-list">
			<Menu name="statistics" on:selectMenu={selectMenu} selected={currentRoute==='statistics'}>Statistics</Menu>
			
			
				{#each modules as module}
					<Menu name="{module.guid}" on:selectMenu={selectModule} expandable=true
						selected={currentRoute===module.guid} bind:expanded={module.expanded}>{module.name}
					</Menu>
					{#if module.expanded == true}
					<li>
					<ul>
						
						{#each module.baselines as baseline}
							<Menu name="{module.guid +'|' + baseline.guid}" 
								selected={currentRoute=== (module.guid +'|' + baseline.guid)} 
								on:selectMenu={selectBaseline} {baseline}>{baseline.name}</Menu>
						{/each}
					</ul>
					</li>
					{/if}

				{/each}
			
			<Menu name="reports" on:selectMenu={selectMenu} selected={currentRoute==='reports'}>Reports</Menu>
			<Menu name="help" on:selectMenu={selectMenu} selected={currentRoute==='help'}>Help</Menu>
			<p class="menu-label">Setup</p>
			<li>
				<ul>
					<Menu name="preferences" on:selectMenu={selectMenu} selected={currentRoute==='preferences'}>Preferences</Menu>
					<Menu name="password" on:selectMenu={selectMenu} selected={currentRoute==='password'}>Change Password</Menu>
				</ul>
			</li>
		</ul>
	</aside>
	{#if modules}
		<main class="tile is-parent is-vertical is-10 is-fullwidth">
			<svelte:component this={currentComponent} on:route={handleRoute} on:import={showImportDialog}
				on:saveModule={saveModule}
				moduleCount={moduleCount} baselineCount={baselineCount} moduleName={currentModuleName} />
		</main>
	{/if}
	<ImportFile bind:isActive={importModal}/>
</div>

<div class="footer level">
	<div class="level-left">Chanter Requirements System, Version {version}</div>
	<div class="level-right">Copyright 2020 © DaT Systems</div>
</div>
