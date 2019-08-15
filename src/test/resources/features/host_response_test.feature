@petstore
Feature: Resource response tester

  Scenario Outline: Resource should satisfy time requirements
    When I ping host '<host>'
    Then Host '<host>' should response in time

    Examples:
      | host              |
      | www.pethouse.ua   |
      | www.zootovary.com |