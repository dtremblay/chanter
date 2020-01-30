<script>
    export let isActive=false;
    let filenames;

    function close() {
        isActive = false;
    }
</script>
<style>
    
</style>
<div class="modal" class:is-active={isActive}>
    <div class="modal-background" />

    <div class="modal-card">
        <header class="modal-card-head">
            <p class="modal-card-title">Create New Module from File</p>
            <button class="delete" aria-label="close" on:click={close}></button>
        </header>
        <section class="modal-card-body">
            <div class="content">
                <div class="field">
                    <label class="label">Module Name
                        <div class="control has-icons-left has-icons-right">
                            <input class="input" type="text" placeholder="New Module Name"/>
                        </div>
                    </label>
                </div>

                <div class="field">
                    <div class="file has-name">
                        <label class="file-label">
                            <input class="file-input" type="file" bind:files={filenames} />
                            <span class="file-cta">
                            <span class="file-icon">
                                <i class="fas fa-upload" />
                            </span>
                            <span class="file-label">Choose a file…</span>
                            </span>
                            {#if filenames && filenames[0]}
                                <span class="file-name">{filenames[0].name}</span>
                            {/if}
                            
                        </label>
                    </div>
                </div>
            </div>
        </section>
        <footer class="modal-card-foot">
            <button class="button is-success">Import</button>
            <button class="button" on:click={close}>Cancel</button>
        </footer>
    </div>
</div>
