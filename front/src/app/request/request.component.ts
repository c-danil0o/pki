import {Component, Input} from '@angular/core';
import {BadgeModule} from "primeng/badge";
import {CardModule} from "primeng/card";
import {DatePipe} from "@angular/common";
import {SharedModule} from "primeng/api";
import {Request} from "../models/Request";

@Component({
  selector: 'app-request',
  standalone: true,
    imports: [
        BadgeModule,
        CardModule,
        DatePipe,
        SharedModule
    ],
  templateUrl: './request.component.html',
  styleUrl: './request.component.css'
})
export class RequestComponent {

  syntaxHighlight(json: string) {
    if (typeof json != 'string') {
      json = JSON.stringify(json, undefined, 2);
    }
    json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
    return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
      var cls = 'number';
      if (/^"/.test(match)) {
        if (/:$/.test(match)) {
          cls = 'key';
        } else {
          cls = 'string';
        }
      } else if (/true|false/.test(match)) {
        cls = 'boolean';
      } else if (/null/.test(match)) {
        cls = 'null';
      }
      return '<span class="' + cls + '">' + match + '</span>';
    });
  }


  protected readonly JSON = JSON;
  @Input() request!: Request;
}
