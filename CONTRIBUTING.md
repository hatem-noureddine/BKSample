# Contributing to BankTest

Thank you for your interest in contributing to BankTest! We welcome contributions from the community to help make this project better.

## code of Conduct

Please note that this project is released with a [Code of Conduct](CODE_OF_CONDUCT.md). By participating in this project you agree to abide by its terms.

## How to Contribute

1.  **Fork the Repository**: Create your own copy of the project.
2.  **Create a Branch**: Create a new branch for your feature or bug fix (`git checkout -b feature/amazing-feature`).
3.  **Make Changes**: Implement your changes, adhering to the project's coding standards.
4.  **Test**: Ensure all unit tests and instrumentation tests pass (`./gradlew test connectedAndroidTest`).
5.  **Lint**: Run static analysis tools to ensure code quality (`./gradlew detekt ktlintCheck`).
6.  **Commit**: Commit your changes with clear, descriptive messages.
7.  **Push**: Push your branch to your fork.
8.  **Open a Pull Request**: Submit a PR to the `main` branch of this repository.

## Coding Standards

-   **Kotlin**: We follow the official [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html).
-   **Linting**: The project uses **Ktlint** and **Detekt**. Please run them before submitting.
-   **Architecture**: Respect the **Clean Architecture** layers. Do not bypass the Domain layer.
-   **Testing**: New features should include unit tests. UI changes should include previews and/or instrumentation tests.

## CI/CD Pipeline

We use GitHub Actions to ensure code quality on every Pull Request.
For a detailed explanation of the pipeline, workflows, and jobs, please refer to [CI/CD Documentation](docs/CI_CD.md).


## Reporting Issues

If you find a bug or have a feature request, please open an issue in the standard GitHub Issues section of the repository. Include as much detail as possible (logs, screenshots, reproduction steps).

## License

By contributing, you agree that your contributions will be licensed under its MIT License.
