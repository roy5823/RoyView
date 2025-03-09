# RoyCad DWG Viewer - GitHub Setup Guide

## Step 1: Create GitHub Account
1. Go to [GitHub.com](https://github.com)
2. Click "Sign Up" in the top right
3. Follow the registration process

## Step 2: Create New Repository
1. Click the "+" icon in the top right corner
2. Select "New repository"
3. Fill in repository details:
   - Repository name: `RoyCad_DWGViewer`
   - Description: `Android DWG Viewer Application`
   - Select "Public"
   - Do NOT initialize with README
   - Click "Create repository"

## Step 3: Upload Project Files
1. After creating the repository, you'll see the "Quick setup" page
2. Click "uploading an existing file"
3. Extract the RoyCad_DWGViewer.zip on your computer
4. Drag and drop ALL files from the extracted folder
5. Click "Commit changes"

## Step 4: Enable GitHub Actions
1. Go to "Settings" tab in your repository
2. Click "Actions" in the left sidebar under "Security"
3. Under "Actions permissions":
   - Select "Allow all actions and reusable workflows"
   - Click "Save"

## Step 5: Configure Repository Settings
1. Stay in "Settings" tab
2. Under "General" in left sidebar:
   - Scroll down to "Features"
   - Enable "Issues" (if you want bug tracking)
   - Enable "Discussions" (if you want user feedback)
3. Under "Security":
   - Enable "Dependabot alerts" for security updates

## Step 6: Get Your APK
1. After uploading files, go to "Actions" tab
2. Wait for the workflow "Android CI" to complete
3. Click on the completed workflow
4. Scroll down to "Artifacts"
5. Click "app-debug" to download the APK

## Step 7: Install APK
1. Transfer the downloaded APK to your phone
2. On your phone:
   - Go to Settings > Security
   - Enable "Unknown sources" or "Install unknown apps"
3. Open the APK file
4. Click "Install"

## Troubleshooting
If the build fails:
1. Check Actions tab for error details
2. Verify all files were uploaded correctly
3. Make sure GitHub Actions is enabled
4. Try re-running the workflow:
   - Go to Actions tab
   - Click on the failed workflow
   - Click "Re-run jobs" button

## Repository Structure
```
RoyCad_DWGViewer/
├── .github/workflows/    # GitHub Actions configuration
├── app/                  # Android app module
│   ├── src/             # Source code
│   └── build.gradle     # App-level build config
├── gradle/wrapper/      # Gradle wrapper files
├── build.gradle         # Project-level build config
└── settings.gradle      # Project settings
```

## Security Note
- The APK is debug-signed
- Suitable for testing
- Requires Android 5.0 or higher
- Will request storage permissions for DWG files
