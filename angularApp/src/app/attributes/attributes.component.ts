import {Component, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-attributes',
  templateUrl: './attributes.component.html',
  styleUrls: ['./attributes.component.css']
})
export class AttributesComponent implements OnInit {
  @Output() treeData;
  policyName: String;
  policyVersion: String;
  constructor() { }

  ngOnInit() {
  }

}
