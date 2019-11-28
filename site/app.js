import { h, Component, render } from "https://unpkg.com/preact?module";
import htm from "https://unpkg.com/htm?module";
import { Router, Link } from "https://unpkg.com/preact-router?module";

// Initialize htm with Preact
const html = htm.bind(h);

const Page = () =>
  html`
    <h1>Test page</h1>
  `;
const Mobile = () =>
  html`
    <h1>Mobile page</h1>
  `;

const app = html`
	<div class="app">
        <h1>TEST</h1>
        <${Page}/>
        <MyLink/>
        <${Link} href="/mobile/pb_wi/12b">12b@pb_wi</${Link}>
        <br/>
        <${Link} href="/">home</${Link}>
        <${Router}>
            <${Mobile} path="/mobile/pb_wi/12b" />
            <${Page} path="/" default/>
        </${Router}>
    </div>
    `;
render(app, document.getElementById("app"));
