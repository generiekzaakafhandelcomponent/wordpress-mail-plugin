# Plugin Documentation

## Overview

This plugin sends emails through the DenHaag Wordpress Mail service, using templates managed on the Wordpress side. It supports sending a templated email, optionally with attachments sourced from the Documenten API.

## Dependencies

### Backend

```kotlin
dependencies {
    implementation("com.ritense.valtimoplugins:wordpress-mail-plugin:1.0.0")
}
```

### Frontend

```json
{
  "dependencies": {
    "@valtimo-plugins/wordpress-mail-plugin": "1.0.0"
  }
}
```

In your `app.module.ts`:

```typescript
import {
    WordpressMailPluginModule, wordpressMailPluginSpecification,
} from '@valtimo-plugins/wordpress-mail-plugin';

@NgModule({
    imports: [
        WordpressMailPluginModule,
    ],
    providers: [
        {
            provide: PLUGIN_TOKEN,
            useValue: [
                wordpressMailPluginSpecification,
            ]
        }
    ]
})
```

## Configuration

| Property | Type   | Required | Description                                  |
|----------|--------|----------|-----------------------------------------------|
| baseUrl  | string | Yes      | The base URL of the Wordpress Mail service    |

## Actions

### Send email

Sends a templated email through the Wordpress Mail service.

| Parameter          | Type                  | Required | Description                                             |
|---------------------|-----------------------|----------|----------------------------------------------------------|
| templateData        | TemplateDataEntry[]   | Yes      | Key/value pairs resolved into the email template          |
| mailSendTaskFrom     | string                | Yes      | Sender email address                                      |
| mailSendTaskTemplate | string                | Yes      | Name of the Wordpress email template to use                |
| mailSendTaskSubject  | string                | Yes      | Email subject                                              |
| mailSendTaskTo       | string                | Yes      | Recipient email address                                    |

### Send email with attachment

Sends a templated email with one or more document attachments.

| Parameter          | Type                  | Required | Description                                                          |
|---------------------|-----------------------|----------|------------------------------------------------------------------------|
| documentUrls         | string[]              | Yes      | URLs of the documents (Documenten API) to attach                       |
| templateData         | TemplateDataEntry[]   | Yes      | Key/value pairs resolved into the email template                       |
| mailSendTaskFrom     | string                | Yes      | Sender email address                                                    |
| mailSendTaskTemplate | string                | Yes      | Name of the Wordpress email template to use                             |
| mailSendTaskSubject  | string                | Yes      | Email subject                                                           |
| mailSendTaskTo       | string                | Yes      | Recipient email address                                                 |

## Usage

Link either action to a service task in a BPMN process. The action resolves `templateData` and the mail properties against the process/document context, then calls the Wordpress Mail REST API (`wp-json/email/v1/*`) using the configured plugin `baseUrl`.
