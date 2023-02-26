//import './app.css'
import App from './App.svelte';
import { version } from '../package.json';

const app = new App({
  target: document.getElementById('app'),
  props: {
		version: version,
		baseServer: "http://localhost:8001/chanter",
		path: window.location
	}
});

export default app;
