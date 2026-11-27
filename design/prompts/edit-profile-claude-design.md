# Design request: "Edit profile" for dz

## Context
dz is a mobile reading app: a personal library, a reader, a store, and a light social layer around what friends are reading. It has been fully redesigned in the **Organic** design system, and 37 screens already exist (attached). I need **one new screen, Edit profile**, plus its sheets, dialogs and states. It must look like it was designed in the same pass as the other 37. Someone flipping between Settings, Profile, Edit collection and this screen should not be able to tell it was added later.

- **Entry:** Settings (screen 25) → the account card at the top → its "Edit" pill.
- **Exit:** back to Settings. The changes appear on Profile (screen 21). The banner will be added to the Profile header later.
- No tab bar on this screen. It is pushed from Settings, like Edit collection.

## Look at these existing screens before designing
- **14 Edit collection** is the closest pattern, so follow it:
  - top bar with "Cancel" on the left, the Caprasimo title in the centre and "Save" on the right in the accent;
  - an uppercase kicker label above each field;
  - filled pill inputs, with a 2px accent outline when focused;
  - the multi-line description card;
  - the toggle inside a filled card.
- **25 Settings:** the account card (sage tint, terracotta avatar with initial, "Edit" pill), section kickers, and rows with icon + label + trailing value + chevron.
- **27 Friend detail:** the sage identity header with a large circular avatar. Reuse its proportions for the banner + photo block.
- **06 Sign up:** filled pill text fields with a trailing text action ("Show").
- **17 Reading settings:** the bottom sheet pattern (28px top radius, grab handle, Caprasimo title, kickers, dimmed page behind).
- **21 Profile:** the avatar style (accent-200 circle, initial in the heading font).

## Design system rules (non-negotiable)
- **Tokens only**, from Organic `styles.css`. Never hard-code a value the tokens already carry.
  - Background `--color-bg` #f5ead8, surface #ebddc5, text #201e1d.
  - Terracotta accent #c67139. Accent text at body size uses `--color-accent-700` #8c491a for contrast.
  - Sage `--color-accent-2` #7a8a5e is a real second voice, not just a highlight.
  - Inputs are filled `--color-neutral-100` #f9f4ed with no border until focus.
- **Type:** Caprasimo 400 for headings only, Figtree 400/600/700 for everything else. Kickers are 10–12px, weight 800, letter-spacing 0.08–0.1em, `--color-neutral-700`.
- **Shape:**
  - Cards and sheets are 28px radius, always filled, never outlined. No hairlines or divider lines; whitespace separates.
  - Single-line inputs are pills; multi-line inputs use 16px radius.
  - Buttons are pills, and a primary full-width button is 58px tall.
  - Toggles are 52×30 (off `--color-neutral-300`, on accent, white 24px knob).
- **Icons:** Lucide at stroke-width 2.75.
- **Frame:** 390×844 with a 24px side gutter, and the same status bar as the other screens.
- **Layout:** left-aligned and asymmetric, with generous air. Don't crowd the rounded shapes.
- **Images:** photos and banners are placeholders: initials, warm gradients, soft circles or blobs. No stock photos. When a photo is shown, give it the system's "washed" treatment with rounded edges.
- **Components:** don't invent a new kind of component when an existing one fits.

## Content, top to bottom
Split the form into clearly separate groups so people understand **who sees what**.

### 1. Photos (top of the scroll area)
- **Banner photo:** a full-width rounded block (28px radius), about 3:1 (around 342×114).
  - Empty state: a warm sage or terracotta tinted fill with soft circle/blob decoration, like the circle on Profile's streak card. Never a grey box.
  - A small circular camera button sits on the banner.
  - This banner will later sit at the top of the Profile screen, so it should work there too.
- **Profile photo:** a large circle (about 96px) overlapping the bottom-left edge of the banner, with a 4px ring in the background colour.
  - Empty state: the initial in Caprasimo.
  - A small accent-coloured circular camera badge sits on it.
- Tapping either one opens a bottom sheet: "Take photo", "Choose from library", and "Remove photo" (shown only when a photo exists, styled as destructive).

### 2. On your profile (anyone who can see the profile sees these)
- **Full name:** single-line pill. Required.
- **Username:** pill with a fixed "@" prefix inside the field.
  - Helper text underneath gives the rules: letters, numbers, "." and "_", 3–30 characters.
  - Inline availability states: checking (small spinner), available (sage check + "Available"), taken (accent-700 text: "That username is taken").
- **Bio:** multi-line filled card, like Edit collection's description. Placeholder along the lines of "A line or two about what you read". Live counter at the bottom right, e.g. "84 / 500".
- **Speciality** (what the person does: their job, or where and what they study):
  - a two-option segmented control, "Work · Study", followed by one pill field;
  - its placeholder changes with the choice, e.g. "Product designer at a studio" / "Literature student, University of Edinburgh".
- **Profile visibility:** two selectable cards, Open and Private, each with an icon, a title and one line saying what it means:
  - Open: "Anyone on dz can see your profile and shelves."
  - Private: "Only friends you accept can see your profile."
  - The selected card gets an accent-tinted fill and an accent ring.
  - Settings already has a separate "Who sees my shelves" row. Keep this about the profile as a whole; don't repeat shelf settings here.

### 3. Private details
Use the kicker "PRIVATE · ONLY YOU SEE THESE" with a small lock icon.
- **Email:** read-only row showing the address, with a sage "Verified" tag and a "Change" text action. Changing it goes through the existing code-verification flow (screen 08), so it is never edited inline here.
- **Phone number:** pill with a country-code chip on the left (flag + "+44" + chevron) and the number on the right.
- **Date of birth:** a row that opens a date picker bottom sheet. **Can be set only once.**
- **Gender:** a row that opens a picker sheet with Female, Male, Prefer not to say. **Can be set only once.**
- **Address:** one grouped filled card with Country (opens a picker), City, Street address and Postal code. All optional.

### One-time fields (date of birth, gender): design both states
- **Not set yet:** a normal tappable row, with a small accent-700 helper underneath: "You can set this once. It can't be changed later."
- **Already set:**
  - the value sits on a quieter fill (`--color-neutral-200`) with a lock icon at the end;
  - helper text: "Set once. Contact support to change it.";
  - not tappable.
- **First save that includes one of them:** a confirmation dialog in the Organic dialog style (28px radius, Caprasimo title).
  - Title "Save your date of birth?". The body repeats the value and says it can't be changed later.
  - Actions: "Go back" (secondary) and "Save" (primary).

### Saving
- Save lives top right, exactly as in Edit collection. No second Save button at the bottom.
- Save is disabled (45% opacity) while nothing has changed or a field is invalid.
- Leave at least 40px of air under the last field.

## Frames to deliver
1. **Default:** a fully filled profile for the same sample person as the other screens: Amelia Hart, @amelia.reads, amelia@hart.co, Premium member.
2. **Scrolled:** the lower half of the form (private details), if it doesn't fit in frame 1.
3. **New account:** no photos, mostly empty fields, one-time fields not set.
4. **Keyboard open:** the username field focused, showing the "taken" error.
5. **Photo source** bottom sheet.
6. **Date of birth** picker bottom sheet.
7. **Gender** picker bottom sheet.
8. **One-time confirmation** dialog.
9. **Unsaved changes:** tapping Cancel after editing opens a "Discard changes?" dialog with "Keep editing" / "Discard".
10. **Saving and saved:** Save shows a small spinner and the fields are disabled. Then a short Organic-style "Profile updated" message back on Settings.

## Copy and tone
Short, warm and plain, like the existing screens ("Make a shelf", "Two minutes, then we'll find you something to read."). Use sentence case. Every error says what to do next.

## Build constraints
- The screen will be built in Compose Multiplatform (Android + iOS) from existing Organic components: back button, screen header, text field + field label, kicker, card, toggle, checkbox, primary button, avatar, list row with chevron, and the bottom-sheet pattern. Prefer these.
- If something needs a new component (segmented control, country-code chip, banner, selectable option card), build it from the same tokens and list it as new.
- Accessibility:
  - tap targets of at least 44px;
  - labels always visible, never placeholder-only;
  - errors never shown by colour alone;
  - accent text at body size uses accent-700.
- Say what you assumed wherever the request doesn't answer something.

## Output
- The frames above at 390×844, in the same canvas style as the existing "DZ All Screens" file.
- Under each frame, a short "What each part does" annotation card, like the existing handoff.
- A list of any new components with their specs (size, radius, tokens, states).
