import App from './App.svelte';
import { version } from '../package.json';

const app = new App({
	target: document.body,
	props: {
		version: version,
		baseServer: "http://localhost:8001/chanter",
		path: window.location
	}
});

export default app;