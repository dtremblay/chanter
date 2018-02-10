angular.module('requirements', ['ui.grid','ui.grid.resizeColumns'])
.controller('RequirementsController',
    RequirementsController);

function RequirementsController($scope) {
  var requirements = [
    {id:"req-1", title: "my heading", text: "Not applicable", type:"header", options: {}},
    {id:"req-12", title: "Do I need a title?", text: "my requirement", type:"text", options: {category:"database"}},
    {id:"req-13", title: "Do I need a title?", text: "my requirement also", type:"text", options: {category:"ui"}}
  ];
  $scope.gridOptions = {
    columnDefs: [
      { field: 'id', width: '13%' },
      { field: 'title', width: '25%'}
    ],
    data: requirements
  };
}