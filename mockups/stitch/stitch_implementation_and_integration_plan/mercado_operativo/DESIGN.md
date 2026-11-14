---
name: Mercado Operativo
colors:
  surface: '#f7f9ff'
  surface-dim: '#d1dbe8'
  surface-bright: '#f7f9ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#edf4ff'
  surface-container: '#e4effd'
  surface-container-high: '#dfe9f7'
  surface-container-highest: '#d9e3f1'
  on-surface: '#121d26'
  on-surface-variant: '#3f4941'
  inverse-surface: '#27313c'
  inverse-on-surface: '#e8f2ff'
  outline: '#6f7a70'
  outline-variant: '#becabe'
  surface-tint: '#006d3d'
  primary: '#006438'
  on-primary: '#ffffff'
  primary-container: '#1b7f4c'
  on-primary-container: '#d1ffdb'
  inverse-primary: '#7dda9e'
  secondary: '#0f6682'
  on-secondary: '#ffffff'
  secondary-container: '#98defe'
  on-secondary-container: '#07637f'
  tertiary: '#15643c'
  on-tertiary: '#ffffff'
  tertiary-container: '#347d52'
  on-tertiary-container: '#d2ffdc'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#99f6b8'
  primary-fixed-dim: '#7dda9e'
  on-primary-fixed: '#00210f'
  on-primary-fixed-variant: '#00522d'
  secondary-fixed: '#bde9ff'
  secondary-fixed-dim: '#8ad0ef'
  on-secondary-fixed: '#001f2a'
  on-secondary-fixed-variant: '#004d64'
  tertiary-fixed: '#a7f3bf'
  tertiary-fixed-dim: '#8cd7a5'
  on-tertiary-fixed: '#00210f'
  on-tertiary-fixed-variant: '#00522e'
  background: '#f7f9ff'
  on-background: '#121d26'
  surface-variant: '#d9e3f1'
  surface-bg: '#F5F7F6'
  surface-card: '#FFFFFF'
  text-muted: '#52606D'
  border-subtle: '#D9E0DC'
  status-success: '#2E7D32'
  status-warning: '#B45309'
  status-danger: '#B91C1C'
typography:
  headline-xl:
    fontFamily: Inter
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
    letterSpacing: -0.02em
  headline-xl-mobile:
    fontFamily: Inter
    fontSize: 26px
    fontWeight: '700'
    lineHeight: 32px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Inter
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
    letterSpacing: -0.01em
  headline-md:
    fontFamily: Inter
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 26px
  kpi-value:
    fontFamily: Inter
    fontSize: 30px
    fontWeight: '700'
    lineHeight: 36px
    letterSpacing: -0.02em
  kpi-value-mobile:
    fontFamily: Inter
    fontSize: 24px
    fontWeight: '700'
    lineHeight: 30px
    letterSpacing: -0.01em
  body-lg:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  body-sm:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '400'
    lineHeight: 16px
  table-data:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '500'
    lineHeight: 20px
  label-md:
    fontFamily: Inter
    fontSize: 13px
    fontWeight: '600'
    lineHeight: 16px
  label-sm:
    fontFamily: Inter
    fontSize: 11px
    fontWeight: '600'
    lineHeight: 14px
    letterSpacing: 0.04em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  gutter: 1rem
  gutter-desktop: 1.5rem
  margin: 1rem
  margin-desktop: 1.5rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2rem
  space-2xl: 3rem
---

## Brand & Style

The design system is engineered for neighborhood retail operations and countertop grocery management. It projects an atmosphere that is sober, highly utilitarian, dependable, and approachable. Rather than adopting cold corporate metrics or playful consumer motifs, the visual signature emphasizes operational clarity, instant numerical scanning, and tactile calm under busy retail shifts.

The aesthetic follows **Functional Modernism with Crisp Editorial Structuring**. It relies on high-contrast tabular typography, subtle hairline containment, structural cards, and a crisp palette anchored in market green and deep petroleum blue. Visual noise, decorative illustrations, and heavy drop shadows are eliminated in favor of information density, predictable button ergonomics, and crystal-clear data hierarchies designed for rapid decision-making in retail management.

## Colors

The color palette is calibrated for long operational hours under bright store lighting, maintaining WCAG AA contrast compliance across all interactive elements and statistical displays.

- **Primary (`#1B7F4C` - Verde mercado):** Drives key transactional intents, active tabs, primary confirmation actions, and brand identity touchpoints.
- **Secondary (`#1F6F8B` - Azul petróleo):** Provides non-competing analytical depth, used for secondary graph vectors, category distributions, informational badges, and interactive secondary accents.
- **Tertiary (`#14633B` - Verde profundo):** Serves as the precise hover and active pressed state for primary controls.
- **Neutral (`#1F2933`):** High-density dark charcoal for primary copy, column headers, and monetary figures to ensure immediate legibility.
- **Canvas & Surface (`#F5F7F6` / `#FFFFFF`):** A subtle, neutral grey-green tinted canvas that isolates high-clarity white surface containers without harsh blinding glare.
- **Semantic Accents:** 
  - `status-success` (`#2E7D32`) denotes completed transactions and positive margin trends.
  - `status-warning` (`#B45309`) flags low stock alerts and urgent restock states.
  - `status-danger` (`#B91C1C`) isolates destructive cancellation triggers, annulled orders, and negative trends.

Status indicators must always pair color with text labels or iconography to guarantee unambiguous accessibility.

## Typography

Typography prioritizes tabular scanning speed, horizontal alignment, and strict hierarchical contrast. **Inter** is the designated typeface across all levels, configured with enabled tabular figures (`font-variant-numeric: tabular-nums; tnum`) for financial data, SKU identifiers, timestamps, and stock tallies.

### Typographic Rules:
- **Numerical Hierarchy:** Monetary values in Colombian Pesos (`COP`) and KPI metrics must render in semi-bold or bold tabular figures, preventing column shifts when counters update dynamically.
- **Alignments:** Numeric data in tables (totals, units, unit costs, discounts) must always be right-aligned with corresponding header text aligned identically. Descriptive names and categories remain left-aligned.
- **Labels & Badges:** `label-sm` is uppercase with slight tracking (`+0.04em`) to establish visual separation between metadata pills and standard paragraph body text.

## Layout & Spacing

The layout is built on an **8px base grid rhythm** optimized for desktop productivity while providing structured responsiveness down to 360px mobile viewports.

### Grid & Ergonomics:
- **Desktop Architecture (≥ 1024px):** Fixed left navigation rail (width: 240px) paired with a fluid main workspace bounded by a max-width container of 1440px. Grid sections use 12 columns with `1.5rem` (24px) gutters and `1.5rem` (24px) external canvas margins.
- **Mobile Architecture (< 1024px):** The navigation rail compresses into a compact top header with a collapsible off-canvas drawer. Layout shifts to a single column flow with `1rem` (16px) margins and gutters.
- **Dense Operational Areas:** Point of Sale (Nueva Venta) uses a split layout on desktop: an 8-column product selector/search surface beside a fixed 4-column live order cart with persistent footer checkout triggers.

## Elevation & Depth

This design system avoids heavy drop shadows, skeuomorphic bevels, or blurry backdrops, which can decrease readability on entry-level retail monitors. Depth is achieved via **low-contrast containment outlines and layered surface tones**:

- **Level 0 (App Canvas):** `#F5F7F6` baseline background.
- **Level 1 (Surface Containers & Tables):** `#FFFFFF` cards, tables, and panels enclosed with a crisp `1px solid #D9E0DC` hairline border. No drop shadow is applied in default states.
- **Level 2 (Dropdowns, Menus & Tooltips):** Float directly above surfaces with a refined, subtle ambient shadow: `0 4px 12px rgba(31, 41, 51, 0.08)`, maintained with the `1px solid #D9E0DC` perimeter border.
- **Level 3 (Modal Confirmation & Drawers):** Backdrop overlay using `rgba(31, 41, 51, 0.4)` with centered modal containers featuring `0 12px 24px rgba(31, 41, 51, 0.12)` and structured hairline boundaries.

## Shapes

The shape system utilizes a standardized **8px border-radius (`roundedness: 2`)** across operational surfaces, balancing utilitarian precision with modern approachability.

- **Standard Cards & Modals:** Standardized to `8px` (`0.5rem`) to frame tabular layouts cleanly without clipping corner cell content.
- **Buttons, Inputs & Selectors:** Exactly `6px` to `8px` to present a reliable, clickable target geometry.
- **Status Pills & Count Badges:** Pill-shaped radius (`9999px`) to immediately distinguish contextual metadata tags from interactive rect-linear buttons.
- **Separators:** 1px hairline dividers utilizing `#D9E0DC` without vertical or horizontal blur.

## Components

### 1. Buttons
- **Primary Action:** Fixed height of 40px, filled with `#1B7F4C`, white semi-bold text, 8px radius, horizontal padding of `1.25rem`. One primary button per active view. Active/Hover: `#14633B`.
- **Secondary Action:** 40px height, transparent background, 1px border in `#1B7F4C`, text in `#1B7F4C`. Hover: background `#F5F7F6`.
- **Destructive Button (Anular):** Ghost or filled styling using `#B91C1C`. Always opens a Level 3 confirmation dialog before execution.
- **Focus State:** 2px ring with 2px offset using `#1F6F8B` for keyboard accessibility.

### 2. Form Inputs & Search Fields
- **Input Fields:** 40px height, background `#FFFFFF`, border `1px solid #D9E0DC`, border-radius 6px, text `#1F2933`. Placeholder text `#52606D`.
- **Labeling:** Persistent label placed directly above the input in `label-md` weight. Contextual validation errors render below the field in `#B91C1C` accompanied by an inline alert icon.
- **Barcode / SKU Quick-search:** Includes integrated clear button (`×`) and auto-focus indicators.

### 3. Data Tables & Lists
- **Structure:** Hairline horizontal rows (`1px solid #D9E0DC`). Header cells styled with `label-sm`, uppercase, `#52606D`, background `#F5F7F6`, and sticky positioning on scroll.
- **Row Hover:** `#F5F7F6` on table body rows for horizontal scanning confidence.
- **Tabular Alignment:** Text left-aligned; all dates, SKU codes, stock quantities, and COP currency figures strictly right-aligned with tabular numerals enabled.

### 4. Metric Cards (KPIs)
- **Container:** White background, 1px `#D9E0DC` border, 16px internal padding, 8px radius.
- **Hierarchy:** Micro-label top (`label-sm`, `#52606D`), KPI value center (`kpi-value`, `#1F2933`), footer trend indicator displaying delta comparison versus previous equivalent period:
  - Positive: `▲ +12,4 %` in `#2E7D32`
  - Negative: `▼ -3,2 %` in `#B91C1C`
  - Neutral / Incomparable: `— Sin base` in `#52606D`

### 5. Status Badges & Chips
- **Sizing:** Height 22px, padding `0 8px`, border-radius `9999px`, typography `label-sm`.
- **COMPLETADA:** Light green background (`#E8F5E9`), text `#2E7D32`.
- **ANULADA:** Light red background (`#FFEBEE`), text `#B91C1C`.
- **Stock Bajo:** Light amber background (`#FEF3C7`), text `#B45309`.
- **Inactivo:** Light grey background (`#ECEFF1`), text `#52606D`.

### 6. Operational Views & System Feedback
- **Live Cart Panel (POS):** Pinned summary calculating running items, item discounts, and dynamic COP grand total in large tabular weight before submission.
- **Empty States:** Clear icon container, descriptive paragraph explaining missing records, and explicit CTA trigger (e.g., "Registrar primera venta").
- **Skeleton Loaders:** Subtle pulsing rectangular blocks in `#EAEFEB` matching final component dimensions to avoid layout shifts.