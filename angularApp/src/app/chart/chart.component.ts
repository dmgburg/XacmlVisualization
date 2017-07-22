import {Component, ElementRef, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import * as d3 from 'd3';
import * as d3hierarchy from 'd3-hierarchy';
import {log} from "util";

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class ChartComponent implements OnInit {
  @ViewChild('chart') private chartContainer: ElementRef;
  private nodeHeight = 200;
  private nodeWidth = 300;
  private separation = 1.1;
  private nodeHorizontalMargin = 20;
  private nodeVerticalMargin = this.nodeWidth * (this.separation - 0.5);
  private testData = "{\"children\":[{\"children\":[{\"children\":[],\"decision\":null,\"description\":\"Policy for any proposal.\",\"id\":\"GPMS-Proposal-Rules\"},{\"children\":[{\"children\":[],\"decision\":null,\"description\":\"Policy for any proposal 2.\",\"id\":\"GPMS-Proposal-Rules2\"}],\"decision\":null,\"description\":\"PolicySet2 for GPMS.\",\"id\":\"GPMS-PolicySet2\"}],\"decision\":null,\"description\":\"PolicySet for GPMS. Only \\\"Tenured/tenure-track faculty\\\" or \\\"Non-tenure-track research faculty\\\" can \\\"Add\\\" a \\\"Whole Proposal\\\" Only \\\"Tenured/tenure-track faculty\\\" or \\\"Non-tenure-track research faculty\\\" can \\\"Add\\\" a \\\"Whole Proposal\\\"\",\"id\":\"GPMS-PolicySet1\"},{\"children\":[{\"children\":[],\"decision\":null,\"description\":\"Policy for any proposal.\",\"id\":\"Arma-Proposal-Rules\"},{\"children\":[{\"children\":[],\"decision\":\"ALLOW\",\"description\":\"Policy for any proposal 2.\",\"id\":\"Arma-Proposal-Rules2\"}],\"decision\":\"ALLOW\",\"description\":\"PolicySet2 for Arma.\",\"id\":\"Arma-PolicySet2\"}],\"decision\":\"ALLOW\",\"description\":\"PolicySet for Arma.\",\"id\":\"Arma-PolicySet1\"}],\"decision\":\"ALLOW\",\"description\":\"Policy resolver root\",\"id\":null}";
  private margin = {top: 20, right: 40, bottom: 20, left: 40};
  constructor() {
  }

  ngOnInit() {
    this.drawChart(this.testData)
  }

  drawChart(treeData: string) {
    console.log(treeData);
    let element = this.chartContainer.nativeElement;

    const hierarchy = d3.hierarchy(JSON.parse(treeData));
    const treeVerticalSize = hierarchy.height + 1;
    const treeHorizontalSize = hierarchy.leaves().length;
    const width = (this.nodeWidth + this.nodeHorizontalMargin) * treeHorizontalSize;
    const height = (this.nodeHeight + this.nodeVerticalMargin) * treeVerticalSize;

    const body = d3.select(element);

    const tree = d3.tree()
      .nodeSize([this.nodeWidth, this.nodeHeight + this.nodeVerticalMargin])
      .separation((a, b) => this.separation);

    const svg = body.append("svg")
      .attr("width", width + this.margin.right + this.margin.left)
      .attr("height", height + this.margin.top + this.margin.bottom)
      .append("g")
      .attr("transform", "translate(" + this.margin.left + "," + this.margin.top + ")");

    const nodes = tree(hierarchy);

    // Declare the nodes…
    let i = 0;
    const node = svg.selectAll("g.node")
      .data(nodes.descendants(), function (d) {
        return d.idHtml || (d.idHtml = ++i);
      });

    // Enter the nodes.
    const varNodeWidth = this.nodeWidth;
    const nodeEnter = node.enter().append("g")
      .attr("class", "node")
      .attr("transform", function (d) {
        return "translate(" + (parseFloat(d.x) + width/2 - varNodeWidth/2) + "," + d.y + ")";
      });

    nodeEnter.append("rect")
      .attr("ry", "6")
      .attr("rx", "6")
      .attr("height", this.nodeHeight)
      .attr("width", this.nodeWidth)
      .style("stroke-width", function (d) {
        switch (d.data.decision) {
          case 'ALLOW':
            return '6';
          case 'DENY':
            return '6';
          default:
            return '1';
        }
      })
      .style("stroke", function (d) {
        switch (d.data.decision) {
          case 'ALLOW':
            return 'green';
          case 'DENY':
            return 'red';
          default:
            return 'grey';
        }
      })
      .style("fill", "none");

    nodeEnter.append("text")
      .attr("x", function (d) {
        return "6"
      })
      .attr("dy", "1.3em")
      .attr("text-anchor", "start")
      .style("fill-opacity", 1)
      .text(function (d) {
        return d.data.description;
      })
      .call(this.wrap, this.nodeWidth);

    // Declare the links…
    const link = svg.selectAll("path.link")
      .data(nodes.descendants().slice(1));

    // Enter the links
    let localNodeHeight = this.nodeHeight;
    let diagonalLink = d3.linkVertical()
      .x(function(d) { return d.x})
      .y(function(d) { return d.y})
      .source(function(d) { return {"x":d.parent.x + width/2, "y":d.parent.y + localNodeHeight}; })
      .target(function(d) { return {"x":d.x + width/2, "y":d.y}; });


    link.enter().insert("path", "g")
      .attr("class", "link")
      .style("stroke-width", function (d) {
        switch (d.data.decision) {
          case 'ALLOW':
            return '6';
          case 'DENY':
            return '6';
          default:
            return '1';
        }
      })
      .style("stroke", function (d) {
        switch (d.decision) {
          case 'ALLOW':
            return 'green';
          case 'DENY':
            return 'red';
          default:
            return 'grey';
        }
      })
      .style("fill", "none")
      .attr("d", diagonalLink );
  }

  wrap(text, width) {
    text.each(function () {
      let text = d3.select(this),
        words = text.text().split(/\s+/).reverse(),
        word,
        line = [],
        lineNumber = 0,
        lineHeight = 1, // ems
        y = text.attr("y"),
        dy = parseFloat(text.attr("dy")),
        tspan = text.text(null).append("tspan").attr("x", 5).attr("y", y).attr("dy", dy + "em");
      while (word = words.pop()) {
        line.push(word);
        tspan.text(line.join(" "));
        if (tspan.node().getComputedTextLength() > width) {
          line.pop();
          tspan.text(line.join(" "));
          line = [word];
          tspan = text.append("tspan").attr("x", 5).attr("y", y).attr("dy", "1.4em").text(word);
        }
      }
    });
  }

}
