import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { ChartComponent } from './chart/chart.component';
import {HttpModule} from '@angular/http';
import {PolicyService} from './checks/policy.service';
import { AttributesComponent } from './attributes/attributes.component';
import {FormsModule} from '@angular/forms';

@NgModule({
  declarations: [
    AppComponent,
    ChartComponent,
    AttributesComponent
  ],
  imports: [BrowserModule, HttpModule, FormsModule],
  providers: [HttpModule, PolicyService],
  bootstrap: [AppComponent]
})
export class AppModule { }
