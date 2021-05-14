/*
 * Activiti Modeler component part of the Activiti project
 * Copyright 2005-2014 Alfresco Software, Ltd. All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

/*
 * Assignment
 */
 var KisBpmAssignmentCtrl = [ '$scope', '$modal', function($scope, $modal) {

    // Config for the modal window
    var opts = {
        template:  'editor-app/configuration/properties/assignment-popup.html?version=' + Date.now(),
        scope: $scope
    };

    // Open the dialog
    $modal(opts);
}];

var KisBpmAssignmentPopupCtrl = [ '$scope', '$http', function($scope, $http) {


    /*begin 获取流程角色数据*/
    var roleUrl = KISBPM.URL.getWorkflowRoles();

    $http({method: 'GET', url: roleUrl}).
    success(function (data, status, headers, config) {
            $scope.groupsData = data;
    }).
    error(function (data, status, headers, config) {
        alert('获取用户信息错误！请查询');
    });
    /*end 获取流程角色数据*/

    // Put json representing assignment on scope
    if ($scope.property.value !== undefined && $scope.property.value !== null
        && $scope.property.value.assignment !== undefined
        && $scope.property.value.assignment !== null) 
    {
        $scope.assignment = $scope.property.value.assignment;
    } else {
        $scope.assignment = {};
    }

    if ($scope.assignment.candidateUsers == undefined || $scope.assignment.candidateUsers.length == 0)
    {
    	$scope.assignment.candidateUsers = [{value: ''}];
    }
    
    // Click handler for + button after enum value
    var userValueIndex = 1;
    $scope.addCandidateUserValue = function(index) {
        $scope.assignment.candidateUsers.splice(index + 1, 0, {value: 'value ' + userValueIndex++});
    };

    // Click handler for - button after enum value
    $scope.removeCandidateUserValue = function(index) {
        $scope.assignment.candidateUsers.splice(index, 1);
    };
    
    if ($scope.assignment.candidateGroups == undefined || $scope.assignment.candidateGroups.length == 0)
    {
    	$scope.assignment.candidateGroups = [{value: ''}];
    }
    
    var groupValueIndex = 1;
    $scope.addCandidateGroupValue = function(index) {
        /*begin modified 新建传入一个空对象*/
        $scope.assignment.candidateGroups.splice(index + 1, 0, {});
        /*end modified 新建传入一个空对象*/
    };

    // Click handler for - button after enum value
    $scope.removeCandidateGroupValue = function(index) {
        $scope.assignment.candidateGroups.splice(index, 1);
    };

    $scope.save = function() {

        $scope.property.value = {};
        handleAssignmentInput($scope);

        /* begin user 和 group 的值为空时删除这个对象，不保存*/
        var userindex=0, groupindex=0;
        if($scope.assignment.candidateUsers && $scope.assignment.candidateUsers.length){
            while(userindex<$scope.assignment.candidateUsers.length){
                if($scope.assignment.candidateUsers[userindex].value === undefined){
                    $scope.assignment.candidateUsers.splice(userindex,1);
                }
                else userindex ++;
            }

        }

        if($scope.assignment.candidateGroups && $scope.assignment.candidateGroups.length){
            while(groupindex<$scope.assignment.candidateGroups.length){
                if($scope.assignment.candidateGroups[groupindex].value === undefined){
                    $scope.assignment.candidateGroups.splice(groupindex,1);
                }
                else groupindex ++;
            }
        }

        /* end user 和 group 的值为空时删除这个对象，不保存*/


        $scope.property.value.assignment = $scope.assignment;
        
        $scope.updatePropertyInModel($scope.property);
        $scope.close();
    };

    // Close button handler
    $scope.close = function() {
    	handleAssignmentInput($scope);
    	$scope.property.mode = 'read';
    	$scope.$hide();
    };
    
    var handleAssignmentInput = function($scope) {
    	if ($scope.assignment.candidateUsers)
    	{
          var emptyUsers = true;
          var toRemoveIndexes = [];
          for (var i = 0; i < $scope.assignment.candidateUsers.length; i++)
          {
              if ($scope.assignment.candidateUsers[i].value != '')
              {
                 emptyUsers = false;
             }
             else
             {
                 toRemoveIndexes[toRemoveIndexes.length] = i;
             }
         }

         for (var i = 0; i < toRemoveIndexes.length; i++)
         {
          $scope.assignment.candidateUsers.splice(toRemoveIndexes[i], 1);
      }

      if (emptyUsers)
      {
          $scope.assignment.candidateUsers = undefined;
      }
  }

  if ($scope.assignment.candidateGroups)
  {
   var emptyGroups = true;
   var toRemoveIndexes = [];
   for (var i = 0; i < $scope.assignment.candidateGroups.length; i++)
   {
      if ($scope.assignment.candidateGroups[i].value != '')
      {
         emptyGroups = false;
     }
     else
     {
         toRemoveIndexes[toRemoveIndexes.length] = i;
     }
 }

 for (var i = 0; i < toRemoveIndexes.length; i++)
 {
  $scope.assignment.candidateGroups.splice(toRemoveIndexes[i], 1);
}

if (emptyGroups)
{
  $scope.assignment.candidateGroups = undefined;
}
}
};
}];