<script>
    // The CreateModule is very simple, it allows users to create a new module. 
    // No data is retrieved from the backend, so no await is required.
    import { createEventDispatcher } from 'svelte';
    import marked from 'marked';

    const dispatch = createEventDispatcher();

    let currentModule = {
        guid: 'new',
        name: "First Module",
        description: "<i>Description</i><hr>Rich Text??",
        baselines:[
            {guid:'new', name:'current', reqIds:[]}
        ],
        attributes:[]
    };

    function saveModule() {
        console.log('save module', currentModule);
        dispatch('saveModule', currentModule);
    }

    function cancelModule() {
        dispatch('route','');
    }

</script>

<div class="is-child box">
    <h1 class="title hero is-info is-light">New Module</h1>
    <div class="container">       

        <div class="field">
            <label class="label">Name</label>
            <div class="control has-icons-right">
                <input class="input is-success" bind:value={currentModule.name} maxlength="200">
                <span class="icon is-small is-right">
                  <i class="fas fa-check"></i>
                </span>
            </div>
            <!--p class="help is-success">This username is available</p-->
        </div>

        <div class="field">
            <label class="label">Description</label>
            <div class="control has-icons-right">
                <textarea class="textarea is-success content" rows="3" bind:value={currentModule.description}></textarea>
                
                <span class="icon is-small is-right">
                  <i class="fas fa-check"></i>
                </span>
            </div>
            <!--p class="help is-success">This username is available</p-->
        </div>

        <div>
            <button class="button is-primary" on:click|preventDefault={saveModule}>Submit</button>
            <button class="button"  on:click|preventDefault={cancelModule}>Cancel</button>
        </div>
        
    </div>
</div>