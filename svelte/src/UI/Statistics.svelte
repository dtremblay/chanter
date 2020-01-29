<script>
  import { createEventDispatcher } from 'svelte';

  const dispatch = createEventDispatcher();

  export let moduleCount;
  export let baselineCount;
  let dropdownActive = false;

  function toggleDropdown() {
    dropdownActive=!dropdownActive;
  }
  function createNewModule() {
    console.log("route to newmodule");
    dispatch('route', 'newmodule')
  }
</script>

<div class="is-child box">
  <h1 class="title hero is-info is-light">Statistics</h1>

  <div class="container">
    <div class="row">
      <table class="table">
        <tbody>
          <tr>
            <th>Modules:</th>
            <td>{moduleCount}</td>
          </tr>
          <tr>
            <th>Baselines:</th>
            <td>{baselineCount}</td>
          </tr>
        </tbody>
        <tfoot>
          <tr>
            <td colspan="2">&nbsp;</td>
          </tr>
        </tfoot>
      </table>
    </div>
    <div>
      <div class="container">
        <button href="#newmodule" class="button" on:click={createNewModule}>New Module</button>

        <!-- Import Dropdown Button -->
        <div class="dropdown" class:is-active={dropdownActive}>
          <div class="dropdown-trigger">
            <button class="button" aria-haspopup="true" aria-controls="dropdown-menu" on:click={toggleDropdown}> 
              <span>Import from File</span>
              <span class="icon is-small">
                <i class="fas fa-angle-down" aria-hidden="true" />
              </span>
            </button>
          </div>
          <div class="dropdown-menu" id="dropdown-menu" role="menu">
            <div class="dropdown-content">
              <a class="dropdown-item"><i class="fas fa-lg fa-file-pdf" ></i>&nbsp;<span>Import from PDF</span></a>
              <a class="dropdown-item"><i class="fas fa-lg fa-file-excel"></i>&nbsp;<span>Import from Excel</span></a>
              <a class="dropdown-item"><i class="fas fa-lg fa-file-csv"></i>&nbsp;<span>Import from CSV</span></a>
              <a class="dropdown-item"><i class="fas fa-lg fa-file-word"></i>&nbsp;<span>Import from MS Word</span></a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
