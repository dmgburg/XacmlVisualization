import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { ChartComponent } from './chart/chart.component';
import {HttpModule} from '@angular/http';
import {ChecksService} from './checks/checks.service';

@NgModule({
  declarations: [
    AppComponent,
    ChartComponent
  ],
  imports: [BrowserModule, HttpModule],
  providers: [HttpModule, ChecksService],
  bootstrap: [AppComponent]
})
export class AppModule { }
