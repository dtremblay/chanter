<script>
    import { createEventDispatcher } from 'svelte';

    const dispatch = createEventDispatcher();
    
    // we will store this for the 
    export let id;
    export let name = "First Requirement";
    export let description = "<i>Description</i><hr>Rich Text??";
    export let baselines;
    export let attributes;

    function saveModule() {
        console.log('save module');
        if (id !== undefined) {
            dispatch('saveModule', {id:id, name:name, description:description,
                baselines: baselines,
                attributes: attributes
            });
        } else {
            dispatch('createModule', {name:name, description:description, baselines: [
                {name:"current", description:"Default baseline"}
            ], attributes: []});
        }
    }

    function cancelModule() {
        dispatch('route','');
    }

</script>
<div class="is-child box">
    <h1 class="title hero is-info is-light">{#if id !== undefined}Edit{:else}New{/if} Module</h1>
    <div class="container">
        <div class="field">
            <label class="label">ID</label>
            <div class="control">
                <input class="input" readonly value={id}>
            </div>
            <!--p class="help is-success">This username is available</p-->
        </div>
        

        <div class="field">
            <label class="label">Name</label>
            <div class="control has-icons-right">
                <input class="input is-success" bind:value={name} maxlength="200">
                <span class="icon is-small is-right">
                  <i class="fas fa-check"></i>
                </span>
            </div>
            <!--p class="help is-success">This username is available</p-->
        </div>

        <div class="field">
            <label class="label">Description</label>
            <div class="control has-icons-right">
                <textarea class="textarea is-success content" rows="3" bind:value={description}></textarea>
                <span class="icon is-small is-right">
                  <i class="fas fa-check"></i>
                </span>
            </div>
            <!--p class="help is-success">This username is available</p-->
        </div>

        <div>
            <button class="button is-primary" on:click|preventDefault={saveModule}>Submit</button>
            <button class="button"  on:click|preventDefault={cancelModule}>Cancel</button>
            {#if id !== undefined}<button class="button is-danger"><span class="icon is-small">
                <i class="fas fa-bold fa-trash-alt"></i>
                </span>
                <span>Delete</span></button>{/if}
        </div>
        
    </div>
</div>

{#if id !== undefined}
    <div class="is-child box">
        <h2 class="title">Baselines</h2>
        <table class="table">
            <thead class="thead">
                <th class="th">Name</th>
                <th class="th">Requirement Count</th>
                <th class="th">Actions</th>
            </thead>
            <tbody class="tbody">
            {#each baselines as baseline}
                <tr class="tr">
                    <td class="td">{baseline.name}</td>
                    <td class="td">{baseline.reqCount}</td>
                    <td class="td"><span class="icon fas fa-file-export" title="Export"></span></td>
                </tr>
            {/each}
            </tbody>
        </table>
    </div>

    <div class="is-child box">
        <h2 class="title"><span>Attributes</span>&nbsp;<button class="button is-info">New Attribute</button></h2>
        <table class="table">
            <thead class="thead">
                <th class="th">Attribute Name</th>
                <th class="th">Attribute Type</th>
                <th class="th">Actions</th>
            </thead>
            <tbody class="tbody">
            {#each baselines as baseline}
                <tr class="tr">
                    <td class="td">{baseline.name}</td>
                    <td class="td">{baseline.reqCount}</td>
                    <td class="td">
                        <span class="icon fas fa-edit" title="Edit"></span>
                        <span class="icon fas fa-trash-alt" title="Delete"></span>
                    </td>
                </tr>
            {/each}
            </tbody>
        </table>
    </div>
{/if}