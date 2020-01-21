<svelte:head>
	<title>Chanter Requirements System</title>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bulma@0.8.0/css/bulma.min.css">
    <script defer src="https://use.fontawesome.com/releases/v5.3.1/js/all.js"></script>
</svelte:head>

<script>

	import Menu from './UI/Menu.svelte';
	import CreateRequirement from './UI/Requirement.svelte';
	import Statistics from './UI/Statistics.svelte';
	import Reports from './UI/Reports.svelte';
	import Help from './UI/Help.svelte';
	import NotImplemented from './UI/NotImplemented.svelte';

	// Setting forms
	import Preferences from './UI/Preferences.svelte';
	import CreateModule from './UI/CreateModule.svelte';
	import PasswordSettings from './UI/NotImplemented.svelte';

	let currentRoute = "statistics";
	let currentComponent = Statistics;
	let currentModule = null;
	export let version; // this should be coming from the package.json
	export let path; // if the user presses refresh, allow the application to return to the correct page
	let menuShown = true;

	let modules = [
		{id:1, name:'test 1', description:'description 1', expanded:false, 
			baselines: [
				{id:11,name:"current",reqCount:10}
			]
		},
		{id:2, name:'test 2', description:'description 2', expanded:false,
			baselines: [
				{id:21,name:"PDR",reqCount:20},
				{id:22,name:"CDR",reqCount:25}
			]
		}
    ];
	
	function resumePath() {
		//extract the path starting from the #
		var hash = path.hash;
		if (hash && hash.length > 1) {
			currentRoute = hash.substring(1);
		}
		locateRoute(currentRoute);
	}
	resumePath();
	function selectMenu(event) {
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
				currentModule = {name:"New Module", description: "Desciption of new module."}
				currentComponent = CreateModule;
				break;
			default:
				//load the module in current module
				currentModule = modules.find(m => m.id === route);
				currentModule.expanded = !currentModule.expanded;
				console.log(currentModule);
				currentComponent = CreateModule;
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
	function addModule(event) {
		var newMod = event.detail;
		newMod.expanded = false;
		modules = [...modules, newMod];
		currentRoute = 'statistics';
		locateRoute('statistics');
	}
	function updateModule(event) {
		var newMod = event.detail;
		newMod.expanded = false;
		var module = modules.find(m => m.id === newMod.id);
		var index = modules.indexOf(module);
		modules.splice(index,1, newMod);
		modules = [...modules];
		currentRoute = 'statistics';
		locateRoute('statistics');
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
				<Menu name="{module.id}" on:selectMenu={selectMenu} expandable=true
					selected={currentRoute===module.id}>{module.name}
					{#if module.expanded}
					<ul>
						{#each module.baselines as baseline}
							<li><Menu name="{baseline.id}" selected={currentRoute===baseline.id} {baseline}>{baseline.name}</Menu></li>
						{/each}
					</ul>
					{/if}
				</Menu>
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

	<main class="tile is-parent is-vertical is-10 is-fullwidth">
		<svelte:component this={currentComponent} on:route={handleRoute} 
			on:createModule={addModule} 
			on:saveModule={updateModule}
			moduleCount={modules.length} {...currentModule} />
	</main>
</div>

<div class="footer level">
	<div class="level-left">Timesheet Reporting System, Version {version}</div>
	<div class="level-right">Copyright 2003-2020 © DaT Systems</div>
</div>
