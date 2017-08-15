import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {Attribute} from './attribute';
import {PolicyService} from "../checks/policy.service";
import {PolicyNode} from "../policy-node";

@Component({
  selector: 'app-attributes',
  templateUrl: './attributes.component.html',
  styleUrls: ['./attributes.component.css']
})
export class AttributesComponent implements OnInit {
  @Output() onTreeData = new EventEmitter<PolicyNode>();
  private POLICY_NAME = "policyName";
  private POLICY_VERSION = "policyVersion";
  constructor(private policyService:  PolicyService) { }

  private attributes: Attribute[];
  ATTRIBUTES: Attribute[] = [
    // { name: 'clientId', value: 'myValue' },
    // { name: 'userId', value: 'myValue'},
    // { name: 'resourceType', value: 'myValue'},
    // { name: 'resourceId', value: 'myValue'},
    // { name: 'funnction', value: 'myValue'},
    // { name: 'location', value: 'myValue'},
    { name: 'policyName', value: 'PolicyArma'},
    { name: 'policyVersion', value: '1' }
  ];

  ATTRIBUTE_NAMES: String[] = [
    'clientId',
    'userId',
    'resourceType',
    'resourceId',
    'funnction',
    'location',
    'policyName',
    'policyVersion'
  ];

  ngOnInit() {
    this.attributes = this.ATTRIBUTES;
  }

  setAttributeName(id: number, name: String){
    this.attributes[id].name = name;
  }

  getPolicy(){
    this.policyService.getPolicy(this.findAttribute(this.POLICY_NAME), this.findAttribute(this.POLICY_VERSION))
      .then(data => {
        this.onTreeData.emit(data);
        console.info('Tree data emitted: ' + data)
      })
  }

  addAttribute() {
    this.attributes.push(new Attribute());
  }

    missingAttributes(): String[]{
    return this.ATTRIBUTE_NAMES.filter(attrName => this.ATTRIBUTES.find(it => it.name !== attrName))
  }

  remove(name: String) {
    this.attributes = this.attributes.filter(attr => attr.name != name)
  }

  private findAttribute(name: String): String{
    const attribute = this.ATTRIBUTES.find(it => it.name === name);
    return attribute ? attribute.value : undefined
  }

}
