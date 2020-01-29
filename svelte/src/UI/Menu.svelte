<script>
    import { createEventDispatcher } from 'svelte';
    export let name = "";
    export let selected = false;
    export let expandable = false;
    export let expanded = false;
    const dispatch = createEventDispatcher();
    function handleRoute() {
        console.log("clicked menu: ", name);
        expanded = true;
        dispatch('selectMenu', {name:name});
    }
</script>
<style>
    span {
        display:block;
    }
</style>
<li on:click|stopPropagation={handleRoute}>
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