<script>
    import { createEventDispatcher } from 'svelte';
    export let name = "";
    export let selected = false;
    export let expandable = false;
    export let expanded = false;
    const dispatch = createEventDispatcher();
    function handleRoute() {
        expanded = !expanded;
        console.log("Menu is expanded: ", expanded);
        dispatch('selectMenu', {name:name});
    }
</script>
<style>
    span {
        display:block;
    }
</style>
<li on:click={handleRoute}>
    <a href="#{name}" class:is-active={selected}>
    {#if expandable}
        {#if expanded}
            - 
        {:else}
            + 
        {/if}
    {/if}
    <slot/></a>
</li>