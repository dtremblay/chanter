<script>
    import { createEventDispatcher } from 'svelte';
    import marked from 'marked';

    const dispatch = createEventDispatcher();
    
    // we will store this for the 
    export let moduleName;
    
    let currentTab = 'Main';
    let showModalAttributes = false;

    function saveModule() {
        console.log('save module', currentModule);
        dispatch('saveModule', currentModule);
    }

    function cancelModule() {
        dispatch('route','');
    }
    function deleteModule() {
        dispatch('deleteModule',currentModule);
    }
    async function loadModule() {
        console.log("Loading module: ", moduleName);
        if (moduleName !== 'new') {
            const response = await fetch('http://localhost:8181/chanter/' + moduleName);
            var currentModule = await response.json();
            console.log("currentModule:",currentModule);
            return currentModule;
        }
    }
    function selectTab(event) {
        currentTab = event.target.innerHTML;
    }
    var newAttribute;
    function addAttribute() {
        newAttribute = {name:'New Attribute', default_value:'', type:'STRING'};
        showModalAttributes = true;
    }
    function cancelAttribute() {
        showModalAttributes = false;
    }
    function saveAttribute(mod) {
        console.log("module: ", mod);
        if (!validate_attribute(newAttribute)) {
            // TODO - add validation code
        } else {
            // persist to the backend

            // Add the attribute to the module - this is not reactive...
            var newAttributes = mod.attributes;
            newAttributes[newAttribute.name] = newAttribute;
            mod.attributes = newAttributes;
            console.log(mod);
        }
        showModalAttributes = false;
    }
    function validate_attribute(a) {
        return true;
    }
</script>

{#await loadModule()}
    <p>Loading...</p>
{:then currentModule}
<div class="is-child box" >
    <h1 class="title hero is-info is-light">Edit Module</h1>
    <div class="tabs is-boxed">
        <ul>
            <li class:is-active={currentTab=='Main'} on:click={selectTab}><a>Main</a></li>
            <li class:is-active={currentTab=='Baselines'} on:click={selectTab}><a>Baselines</a></li>
            <li class:is-active={currentTab=='Attributes'} on:click={selectTab}><a>Attributes</a></li>
        </ul>
    </div>
    {#if currentTab == "Main"}
    <div class="container">
        <div class="field">
            <label class="label">ID</label>
            <div class="control">
                <input class="input" readonly value={currentModule.guid}>
            </div>
        </div>

        <div class="field">
            <label class="label">Name</label>
            <div class="control has-icons-right">
                <input class="input is-success" bind:value={currentModule.name} maxlength="200">
                <span class="icon is-small is-right">
                  <i class="fas fa-check"></i>
                </span>
            </div>
        </div>

        <div class="field">
            <label class="label">Description</label>
            <div class="control has-icons-right">
                <textarea class="textarea is-success content" rows="3" bind:value={currentModule.description}></textarea>
                <span class="icon is-small is-right">
                  <i class="fas fa-check"></i>
                </span>
            </div>
        </div>

        <div>
            <button class="button is-primary" on:click|preventDefault={saveModule}>Submit</button>
            <button class="button"  on:click|preventDefault={cancelModule}>Cancel</button>
            <button class="button is-danger" on:click|preventDefault={deleteModule}>
                <span class="icon is-small">
                    <i class="fas fa-bold fa-trash-alt"></i>
                </span>
                <span>Delete</span>
            </button>
        </div>
    </div>
    {/if}

    {#if currentTab == "Baselines"}
        <button class="button is-info">New Baseline</button>
        <table class="table">
            <thead class="thead">
                <th class="th">Name</th>
                <th class="th">Requirement Count</th>
                <th class="th">Actions</th>
            </thead>
            <tbody class="tbody">
            {#each currentModule.baselines as baseline}
                <tr class="tr">
                    <td class="td">{baseline.name}</td>
                    <td class="td">{baseline.reqIds.length}</td>
                    <td class="td"><span class="icon fas fa-file-export" title="Export"></span></td>
                </tr>
            {/each}
            </tbody>
        </table>
    {/if}
    {#if currentTab == "Attributes"}
        <button class="button is-info" on:click={addAttribute}>New Attribute</button>
        <table class="table">
            <thead class="thead">
                <th class="th">Attribute Name</th>
                <th class="th">Attribute Type</th>
                <th class="th">Default Value</th>
                <th class="th">Actions</th>
            </thead>
            <tbody class="tbody">
            {#each Object.entries(currentModule.attributes) as attribute}
                <tr class="tr">
                    <td class="td">{attribute[1].name}</td>
                    <td class="td">{attribute[1].type}</td>
                    <td class="td">{attribute[1].defaultValue}</td>
                    <td class="td">
                        <span class="icon fas fa-edit" title="Edit"></span>
                        <span class="icon fas fa-trash-alt" title="Delete"></span>
                    </td>
                </tr>
            {:else}
                <tr class="tr"><td class="td">No Attributes</td></tr>
            {/each}
            </tbody>
        </table>
    {/if}

        {#if showModalAttributes}
            <div class="modal is-active">
                <div class="modal-background"></div>
                <div class="modal-card">
                    <header class="modal-card-head">
                    <p class="modal-card-title">Add Attribute</p>
                    <button class="delete" aria-label="close" on:click={cancelAttribute}></button>
                    </header>
                    <section class="modal-card-body">
                    <!-- Content ... -->
                        <div class="field">
                            <label class="label">Name</label>
                            <div class="control">
                                <input class="input" bind:value={newAttribute.name} maxlength="200">
                            </div>
                        </div>

                        <div class="field">
                            <label class="label">Type</label>
                            <div class="select">
                                <select cbind:value={newAttribute.type}>
                                    <option>STRING</option>
                                    <option>INTEGER</option>
                                    <option>BOOLEAN</option>
                                </select>
                            </div>
                        </div>

                        <div class="field">
                            <label class="label">Default Value</label>
                            <div class="control">
                                <input class="input" bind:value={newAttribute.defaultValue} maxlength="200">
                            </div>
                        </div>
                    </section>
                    <footer class="modal-card-foot">
                    <button class="button is-success" on:click={saveAttribute(currentModule)}>Save</button>
                    <button class="button" on:click={cancelAttribute}>Cancel</button>
                    </footer>
                </div>
            </div>
        {/if}


</div>

{:catch error}
    <p style="color: red">{error.message}</p>
{/await}