import { Component, OnInit } from '@angular/core';
import {Attribute} from './attribute';

@Component({
  selector: 'app-attributes',
  templateUrl: './attributes.component.html',
  styleUrls: ['./attributes.component.css']
})
export class AttributesComponent implements OnInit {
  private attributes: Attribute[];
  ATTRIBUTES: Attribute[] = [
    { name: 'Mr. Nice', value: 'myValue' },
    { name: 'Narco' , value: 'myValue'},
    { name: 'Bombasto' , value: 'myValue'},
    { name: 'Celeritas' , value: 'myValue'},
    { name: 'Magneta' , value: 'myValue'},
    { name: 'RubberMan' , value: 'myValue'},
    { name: 'Dynama' , value: 'myValue'},
    { name: 'Dr IQ', value: 'myValue' },
    { name: 'Magma' , value: 'myValue'},
    { name: 'Tornado' , value: 'myValue'}
  ];

  ATTRIBUTE_NAMES: String[] = [
    'clientId',
    'userId',
    'resourceType',
    'resourceId',
    'funnction',
    'location',
  ];
  constructor() { }

  ngOnInit() {
    this.attributes = this.ATTRIBUTES;
  }

  addAttribute() {
    this.attributes.push(new Attribute());
  }

  missingAttributes(){
    this.ATTRIBUTE_NAMES.filter(attrName => this.ATTRIBUTES.find(it => it.name != attrName))
  }

  remove(name: String) {
    this.attributes = this.attributes.filter(attr => attr.name != name)
  }

  onSelect(attribute: Attribute) {
    console.info(attribute.name + " : " + attribute.value )
  }

}
