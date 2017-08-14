import { Component } from '@angular/core';
import {PolicyNode} from "./policy-node";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Abacus policy visualizer';
  public treeData: PolicyNode;

  onTreeDataSet(treeData: PolicyNode){
    console.info('Tree data consumed: ' + treeData);
    this.treeData = treeData
  }
}
