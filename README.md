# Assignment 5 - Unit, Mocking and Integration Testing
**Zyon Nichols  11-01-2025**

## Project Overview
This project implements automated testing and continuous integration using GitHub Actions for the BarnesAndNoble and Amazon packages. The project includes unit tests, integration tests, static analysis with Checkstyle, and code coverage reporting with JaCoCo.

## Build Status
![Build Status](https://github.com/ZNich2/Continuous-Integration-Assignment/actions/workflows/SE333_CI.yml/badge.svg)

## Notes and Responses
### Part 1 - BarnesAndNoble Testing
- Created specification-based tests for BarnesAndNoble functionality
- Created structural-based tests for code coverage

### Part 2 - GitHub Actions Workflow
- Workflow runs on push to main branch
- Includes Checkstyle static analysis
- Includes JaCoCo code coverage
- Generates and uploads artifacts

### Part 3 - Amazon Testing
- Created integration tests for multiple components
- Created unit tests with mocking for isolated testing