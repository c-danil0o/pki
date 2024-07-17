

# Public Key Infrastructure (PKI) System

This project is a comprehensive Public Key Infrastructure (PKI) system built with Java Spring Boot for the backend and Angular for the frontend. It enables users to create and manage different types of certificates, handle certificate signing requests (CSRs), and configure certificate extensions and expiration dates.

## Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Usage](#usage)
- [License](#license)

## Project Overview

The PKI system allows users to create three types of certificates: root, intermediate, and end-entity. Users can view and configure certificate extensions and expiration dates. Additionally, the system manages Certificate Signing Requests (CSRs), enabling users to approve or deny them.

## Features

- **Certificate Management**: Create, view, and manage root, intermediate, and end-entity certificates.
- **Extensions Configuration**: Configure certificate extensions.
- **Expiration Management**: Set and manage certificate expiration dates.
- **CSR Management**: Manage Certificate Signing Requests (CSRs), including approving or denying them.
- **User Roles**: Supports different user roles with appropriate permissions.

## Architecture

The system is designed using a multi-tier architecture, consisting of:

- **Frontend**: Angular application for user interaction.
- **Backend**: Java Spring Boot application for business logic and API management.
- **Database**: Stores user data, certificates, and CSRs.

## Tech Stack

- **Frontend**: Angular
- **Backend**: Java Spring Boot
- **Database**: LDAP (for certificate metadata), PostgreSQL (for CSR), JKS (for certificates)


## Usage

1. **Register and Login:**
    - Visit the homepage and register as a new user.
    - Login with your credentials.

2. **Certificate Management:**
    - Navigate to the certificate management section.
    - Create new certificates (root, intermediate, and end-entity).
    - View details of existing certificates.
    - Configure certificate extensions and expiration dates.

3. **CSR Management:**
    - Navigate to the CSR management section.
    - View pending CSRs.
    - Approve or deny CSRs.


## License

This project is licensed under the MIT License.
